/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myjavapapers.lotusnotes2mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.DxlExporter;
import lotus.domino.EmbeddedObject;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.RichTextItem;
import lotus.domino.Session;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ImageTag;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author kudaraa
 */
public class CopyFilesFromLotusnotesToMongoDB {

    /**
     * @param argv
     *
     */
    public static void main(String argv[]) {
        try {
            /**
             * Conneting to Mongo DB Dev
             */
            Mongo mongoDBConnection = new Mongo(MongoConstants.MONGO_SERVER, MongoConstants.MONGO_PORT);
            DB cimAPPSMongoDb = mongoDBConnection.getDB("cimapps");
            /**
             * Connection and session open with lotus notes.
             */
            Session session = createOrOpenLotusNotesSession();
            /**
             * Lotus notes DB session.
             */
            Database lotusNotesDB = session.getDatabase("JAZ-DA1/Server/Jazz Semiconductor", "FAB/STR");
            //Database lotusNotesDB = session.getDatabase("localhost", "Jazz STR");
            System.out.println("Notes Version : " + session.getNotesVersion());
            System.out.println("Srver name  : " + session.getServerName());
            //session.setConvertMIME(true);
            //   StringBuilder stringBuilderStr = new StringBuilder();
            //Document document = null;
            // boolean firstValue = false;

            if (lotusNotesDB.isOpen()) {
                DocumentCollection lotusNotesDocumentCollection = lotusNotesDB.getAllDocuments();
                System.out.println("Total Files Recevied from Lotus Notes " + lotusNotesDocumentCollection.getCount());
                int totalDocumentsCount = 50;//lotusNotesDocumentCollection.getCount();
                if (totalDocumentsCount > 0) {
                    Document document = lotusNotesDocumentCollection.getFirstDocument();
                    // while (document != null) {
                    while (totalDocumentsCount > 0) {
                        boolean firstItem = false;
                        //  Item documentItem;
                        String str_number = getSTRNumber(document);
                        if (isVallidSTRNumber(str_number)) {
                            System.out.println("HTTP URL :[ " + totalDocumentsCount + "] " + document.getHttpURL());
                            processImagesINDocument(document, session, lotusNotesDB);
                            System.out.println("Items :[ " + totalDocumentsCount + "]" + lotusNotesDocumentCollection.getFirstDocument().getItems());
                            processDocumentData(cimAPPSMongoDb, session, document, firstItem, str_number);

                        }
                        document = lotusNotesDocumentCollection.getNextDocument(document);
                        totalDocumentsCount--;
                    }
                }
            } else {
                Logger.getLogger(LotusNotes2Mongo.class.getName()).log(Level.SEVERE, null, "Lotus Notes FAB/STR did not connect or Mongo is not authenticated");
                //  System.out.println(stringBuilderStr);
            }
//	            document.recycle();
            lotusNotesDB.recycle();
            session.recycle();
            mongoDBConnection.close();
        } catch (UnknownHostException | MongoException | NotesException e) {
            System.out.println("exception = " + e);
            Logger.getLogger(LotusNotes2Mongo.class.getName()).log(Level.SEVERE, null, e);
        } finally {

        }
    }

    private static void processDocumentData(DB cimAPPSMongoDb, Session session, Document document, boolean firstItem, String str_number) {
        StringBuilder stringBuilderStr = new StringBuilder();
        stringBuilderStr.setLength(0);
        try {
            if (document.hasEmbedded()) {
                processDocumentLevelEmbededObjects(document);
            }
            processDocumentItems(cimAPPSMongoDb, session, stringBuilderStr, document, firstItem, str_number);
        } catch (NotesException ex) {
            Logger.getLogger(CopyFilesFromLotusnotesToMongoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        insertIntoMongoDB(cimAPPSMongoDb, stringBuilderStr, str_number);
    }

    private static boolean isVallidSTRNumber(String str_number) {
        return !str_number.isEmpty() && str_number.equalsIgnoreCase("SNPBGE03080000018");
    }

    private static void processDocumentItems(DB cimAPPSMongoDb, Session session, StringBuilder stringBuilderStr, Document document, boolean firstItem, String str_number) throws NotesException {
        // Process Images First
        //  processImagesInDocument(session ,document);
        Iterator<Item> documentItems = document.getItems().iterator();
        HashSet<String> attNames = new HashSet<>();
        while (documentItems.hasNext()) {
            try {
                Item documentItem = documentItems.next();
                attNames.clear();
                String itemName = documentItem.getName(); //this is the name of the field in the doc designer form
                if (itemName.contains("STR_INSTRUCTION")) {
                    System.out.println("Has STR_INSTRUCTION: " + itemName);
                    String iname1 = itemName;
                    Vector values = documentItem.getValues();
                    System.out.println(itemName + " values :" + values);
                    System.out.println("Document Type : " + documentItem.getType());
                }
                if (documentItem.getType() == Item.RICHTEXT) {
                    processEmbeddedObjectsInRichText(cimAPPSMongoDb, attNames, documentItem, str_number);
                }
                printJsonItem(stringBuilderStr, session, documentItem, firstItem, attNames);

                if (documentItem != null) {
                    documentItem.recycle();
                }
                firstItem = false;
            } catch (NotesException ex) {
                Logger.getLogger(CopyFilesFromLotusnotesToMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static Session createOrOpenLotusNotesSession() throws NotesException {
        String host = "jaz-da1.nb.jazzsemi.com";
        String ior = NotesFactory.getIOR(host + ":" + "63148");
        Session session = NotesFactory.createSessionWithIOR(ior, "whitem", "Pnot4u2");
        return session;
    }

    /**
     * @param cimAPPSMongoDb : Mongo DB Database Connection
     * @param stringBuilderStr
     * @param str_number
     */
    private static void insertIntoMongoDB(DB cimAPPSMongoDb, StringBuilder stringBuilderStr, String str_number) {
        stringBuilderStr.append("}");
        String objstr = stringBuilderStr.toString();
        // Mongo DB "CIMAPPS" Database and strdocs collection (table)
        DBCollection strDocsCollection = cimAPPSMongoDb.getCollection("strdocs1");
        // delete document from mongo if it exists....
        BasicDBObject docQuery = new BasicDBObject();
        docQuery.put("_id", str_number);
        WriteResult resultQuery = strDocsCollection.remove(docQuery);
        String resultQueryStr = resultQuery.toString();
        //now re-insert the document
        try {
            DBObject dbobject = (DBObject) JSON.parse(objstr);
            WriteResult result = strDocsCollection.insert(dbobject);
            String resultStr = result.toString();
            String STRinsertError = result.getError();
        } catch (Exception e) {
            System.out.println("exception = " + e);
        }
    }

    private static void processEmbeddedObjectsInRichText(DB cimAPPSMongoDb, HashSet<String> attNames, Item documentItem, String str_number) throws NotesException {
        System.out.println("RichText Item Process Start");

        RichTextItem richTextItem = (RichTextItem) documentItem;
        Vector richTextItemEmbeddedObjects = richTextItem.getEmbeddedObjects(); //lotus notes embedded objects wont pass into arraylists directly
        ArrayList<?> embeddedObjectsFileList = new ArrayList<>(richTextItemEmbeddedObjects);
        System.out.println(" Embedded objects in rich text" + embeddedObjectsFileList.size());
        if (embeddedObjectsFileList.size() > 0) {
            int embeddedObjectSequence = 0;
            for (Object emb1 : embeddedObjectsFileList) {
                EmbeddedObject embObj = (EmbeddedObject) emb1;
                if (!attNames.contains(embObj.getName())) {
                    attNames.add(embObj.getName());
                    System.out.println("** Embedded Object name : " + embObj.getName());
                    System.out.println("Embedded Object Source" + embObj.getSource());
                    System.out.println("Embedded Object Type " + embObj.getType());
                    String embededObjectsLocalPath = "c:\\app\\" + embObj.getSource();
                    if (embObj.getSource().contains(".")) {

                        //**must test to see if the embedded object is a file that can be extracted
                        embObj.extractFile(embededObjectsLocalPath);

                        //** add embedded object to GridFS and then delete the temp file
                        saveEmbeddedObject(documentItem, embObj, embededObjectsLocalPath, str_number, cimAPPSMongoDb, embeddedObjectSequence++); // is only saving the last embedded object in GridFS 
                    } else {
            				//path = path + ".jpg";
//	                                                Stream stream = session.createStream();
//	                                                if (stream.open(path)) {
//	                                                  stream.truncate(); // Any existing file is erased
                        //AgentContext agentContext = session.getAgentContext();
//	                                                  DxlExporter exporter = session.createDxlExporter();
//	                                                  stream.writeText(exporter.exportDxl(db)) ;
                        //OutputStream agentout = this.getAgentOutputStream();

                        //agentout.write("Content-Type: application/octet-stream\n".getBytes());
                        // get an input stream for the attachment
                        //java.io.FileInputStream fileInputStream = new java.io.FileInputStream(path);
                        // write bytes from input stream to output stream
                        //byte[] bytes = new byte[1];
//	                                                    while ( fileInputStream.read(bytes) > -1) {
//
//	                                                    agentout.write(bytes);
//
//	                                                    
//	                                                  String path1 = path;
//	                                                }
                    }
                }
                //embObj.recycle(); 
            }

        }
    }

    private static void processDocumentLevelEmbededObjects(Document document) throws NotesException {
        System.out.println("there are embedded objects in this doc at the document level");
        Vector documentEmbeddedObjectsVector = document.getEmbeddedObjects(); //lotus notes embedded objects wont pass into arraylists directly
        ArrayList<?> docEmbeddedObjList = new ArrayList<>(documentEmbeddedObjectsVector);

        if (docEmbeddedObjList.isEmpty()) {
            //Embedded object is an attachment - not sure but did not find any attachments at the doc level
            String visEmpty = "is empty";
        } else {
            System.out.println("Number Embeded Objects : " + docEmbeddedObjList.size());
            for (int i = 0; i < docEmbeddedObjList.size(); i++) {
                EmbeddedObject eo = (EmbeddedObject) docEmbeddedObjList.get(i);
                String eos = eo.getName() + " of " + eo.getClassName();
                System.out.println("Embedded Object name : " + eos);
            }
        }
    }

    private static String getSTRNumber(Document doc) {
        Item item;
        String Str_number = "";
        Iterator<Item> it;
        try {
            it = doc.getItems().iterator();

            while (it.hasNext()) {
                item = it.next();
                String iname = item.getName(); //this is the name of the field in the doc designer form
                if (iname.contains("STR_Number")) {
                    Str_number = item.getValueString();
                    break;
                }
            }
        } catch (NotesException ex) {
            Logger.getLogger(LotusNotes2Mongo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Str_number;
    }

    private static void saveEmbeddedObject(Item item, EmbeddedObject embObj, String path, String str_number, DB cimAppsMongoDB, int embeddedObjectSequence) {
        try {
            //   Mongo mongo = new Mongo(MongoConstants.MONGO_SERVER, MongoConstants.MONGO_PORT);
            // Logger.getLogger(STRsave.class.getName()).info(  "mongo instance done");
            //   DB db1 = mongo.getDB("cimapps");

            StringBuilder ObjName = new StringBuilder();
            //ObjName.append(str_number).append("-").append(item.getName()).append("-").append(embObj.getName());
            ObjName.append(str_number).append("-").append(item.getName()).append("-").append(embeddedObjectSequence);
            File docFile = new File(path);
            // create a "cimappsSTR" namespace
            GridFS gfscimapps = new GridFS(cimAppsMongoDB, "cimappsSTR1");
            // remove the image file from mongoDB
            // try query not on file name but on the id or use find for the _id
            //gfscimapps.remove(gfscimapps.findOne(ObjName.toString())); //this removes all previous GridFS entries even though it shouldnt
            // get doc file from local drive
            GridFSInputFile gfsFile = gfscimapps.createFile(docFile);
            // set a new filename for identify purpose
            gfsFile.setFilename(ObjName.toString());
            // save the image file into mongoDB
            gfsFile.save();
            // get image file by it's filename
            GridFSDBFile imageForOutput = gfscimapps.findOne(ObjName.toString());
            //imageForOutput.
        } catch (UnknownHostException e) {
            System.out.println("exception = " + e);
            Logger.getLogger(LotusNotes2Mongo.class.getName()).log(Level.SEVERE, null, e);
        } catch (MongoException | IOException | NotesException e) {
            System.out.println("exception = " + e);
            Logger.getLogger(LotusNotes2Mongo.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static void printJsonItem(StringBuilder db1, Session session, Item item, boolean firstItem, HashSet<String> attNames) {

        Vector<?> v;
        Iterator<?> it;
        boolean firstValue = true;

        try {
            if (item != null) {
                String finalsb = "";
                if (item.getType() == Item.AUTHORS
                        || item.getType() == Item.DATETIMES
                        || item.getType() == Item.NAMES
                        || item.getType() == Item.NUMBERS
                        || item.getType() == Item.READERS
                        || item.getType() == Item.RICHTEXT
                        || item.getType() == Item.TEXT) {
                    v = item.getValues();

                    if (v != null && v.size() > 0) {
                        String itemName = item.getName();
                        if (itemName.contains("$")) {
                            itemName = itemName.replace("$", "");
                        }
                        if (attNames.size() > 0) {
                            //if (itemName.contains("Attachments") && attNames.size() > 0) {
                            //there should only be 1 element in the vector so revise it to hold the attachments also
                            StringBuilder sb = new StringBuilder();
                            //sb.append("\"").append(itemName).append("\"" + ": [" );
                            sb.append(" [");
                            //**iterate thru the HashSet to setup the value array 
                            for (String attname : attNames) {
                                sb.append("\"").append(attname).append("\"").append(",");
                            }
                            finalsb = sb.substring(0, sb.length() - 1) + "]";

                            //item.setValues(vct) ;//doesnt seem to work
                        }
                        if (itemName.contains("STR_Number")) {
                            String firstJson = "{\"_id\":\"" + item.getValueString() + "\"";
                            db1.replace(0, 0, firstJson);
                        } else {
                            db1.append(firstItem ? "{\"" : ",\"").append(itemName).append("\":");
                            if (v.size() > 1) {
                                db1.append("[");
                                it = v.iterator();
                                while (it.hasNext()) {
                                    db1.append(firstValue ? "" : ",").append("\"").append(getValueString(session, it.next(), item.getType())).append("\"");
                                    firstValue = false;
                                }
                                // firstValue = true;
                                db1.append("]");
                            } else {
                                if (finalsb.isEmpty()) {

                                    db1.append("\"").append(getValueString(session, v.get(0), item.getType())).append("\"");
                                } else {
                                    // this is for attachments only
                                    db1.append(finalsb);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("exception " + e);
            //logger.info("STR doGet after session******************************************************************" + e );
        }
    }

    private static String getValueString(Session session, Object obj, int type) {

        String valueString = "";

        DateTime dt = null;
        try {

            switch (type) {
                case Item.RICHTEXT:
                    valueString = obj.toString();
                    break;
                case Item.DATETIMES:
                    dt = (DateTime) obj;
                    valueString = dt.getLocalTime();
                    dt.recycle();
                    break;
                case Item.NUMBERS:
                    valueString = obj.toString();
                    break;
                case Item.TEXT:
                    valueString = obj.toString();
                    break;
                default:
                    valueString = obj.toString();
            }

            if (dt != null) {
                dt.recycle();
            }
        } catch (Exception e) {
            System.out.println("exception = " + e);
            //e.printStackTrace();
            // Be quiet.
        }
        return addSlashes(valueString);
        //return (valueString);
    }

    private static String addSlashes(String text) {
        final StringBuffer sb = new StringBuffer(text.length() * 2);
        final StringCharacterIterator iterator = new StringCharacterIterator(
                text);

        char character = iterator.current();

        while (character != StringCharacterIterator.DONE) {
            if (character == '\n') {
                //sb.append("\\n");
                sb.replace(sb.length(), sb.length(), "\\n");
            } else if (character == '\r') {
                sb.replace(sb.length(), sb.length(), "\\r");
            } else if (character == '"') {
                //sb.append("\\\"");
                sb.replace(sb.length(), sb.length(), "");
                //  } else if (character == '\'') {
                //    sb.append("\\\'");
                //  } else if (character == '\\') {
                //     sb.append("\\\\");
                //} else if (character == '\n') {
                //    sb.append("\\n");
                // } else if (character == '{') {
                //    sb.append("\\{");
                //  } else if (character == '}') {
                //     sb.append("\\}");
            } else {
                sb.append(character);
            }

            character = iterator.next();
        }

        return sb.toString();
    }

    private static void processImagesINDocument(Document document, Session session, Database lotusNotesDB) {
        try {
            System.out.println("Proces Image start");
            // Third Approch -- Need to code 
           /* DxlExporter dxlExporter =    session.createDxlExporter();
             String documentExportername = dxlExporter.exportDxl(document);
             dxlExporter.setConvertNotesBitmapsToGIF(true);
             System.out.println("Document Exporter n/* DxlExporter dxlExporter =    session.createDxlExporter();
             String documentExporternameame " + documentExportername);*/

            // First Approch - XML
            String generateXML = document.generateXML();
            getImageData(generateXML);
            // Second Approch - HTML / WEb Page - login failure
            //  processImageUsingHTMLpage(document, session, lotusNotesDB);
            System.out.println("Proces Image Completed");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private static void getImageData(String generateXML) {
        try {
            System.out.println("Processing XMl data recevied from Document");
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder newDocumentBuilder = docBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document documentDOM = newDocumentBuilder.parse(new InputSource(new StringReader(generateXML)));
            if (documentDOM != null) {
                System.out.println("Document DOM parsed successfully");
                String bitMapData = getNotesBitmapData(documentDOM);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CopyFilesFromLotusnotesToMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception : " + ex);
        } catch (SAXException ex) {
            Logger.getLogger(CopyFilesFromLotusnotesToMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception : " + ex);
        } catch (IOException ex) {
            Logger.getLogger(CopyFilesFromLotusnotesToMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception : " + ex);
        }

    }

    private static String getNotesBitmapData(org.w3c.dom.Document documentDOM) {
        String bitmapByteDate = "";
        NodeList elementsByTagName = documentDOM.getElementsByTagName("notesbitmap");
        if (elementsByTagName != null) {
            System.out.println("Number Of Bimap/Inline images in document " + elementsByTagName.getLength());
            for (int imageCount = 0; imageCount < elementsByTagName.getLength(); imageCount++) {
                Node imageNode = elementsByTagName.item(imageCount);
                if (imageNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element imageElement = (Element) imageNode;
                    String imageByteDataStr = imageElement.getTextContent();
                    decodeImagebyteCodeANdWriteImagesToDisk(imageByteDataStr, imageCount);
                }
            }
        }
        return bitmapByteDate;
    }

    private static void decodeImagebyteCodeANdWriteImagesToDisk(String imageByteDataStr, int imageCount) {
        try {
            System.out.println("Writing Image Fils to Disk >> ");
            FileOutputStream outputStream = new FileOutputStream("C:\\app\\inLineImage" + imageCount + ".jpg");
            outputStream.write(imageByteDataStr.getBytes());
            outputStream.close();
            System.out.println("Writing Image Fils to Disk Completed ");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CopyFilesFromLotusnotesToMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception : " + ex);
        } catch (IOException ex) {
            Logger.getLogger(CopyFilesFromLotusnotesToMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception : " + ex);
        }
    }

    private static void processImageUsingHTMLpage(Document document, Session session, Database lotusNotesDB) {
        /* try {
         System.out.println("PRocessing document as webpage start ");
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = dbf.newDocumentBuilder();
         org.w3c.dom.Document htmlDocumentDoc = db.parse(document.getHttpURL());
         NodeList imgs = htmlDocumentDoc.getElementsByTagName("img");
         List<String> srcs = new ArrayList<String>();

         for (int i = 0; i < imgs.getLength(); i++) {
         srcs.add(imgs.item(i).getAttributes().getNamedItem("src").getNodeValue());
         }

         for (String src : srcs) {
         System.out.println("Image Links : "+src);
         }
         } catch (NotesException | ParserConfigurationException | SAXException | IOException ex) {
         Logger.getLogger(CopyFilesFromLotusnotesToMongoDB.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        System.out.println("PRocessing document as webpage start ");
        Collection<String> imageUrls = new ArrayList<String>();

        try {

            URL uriLink = new URL(document.getHttpURL());
            HttpURLConnection con = (HttpURLConnection) uriLink.openConnection();
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("whitem", "Pnot4u2".toCharArray());
                }
            });
           // URLConnection uc = uriLink.openConnection();
           /* String userpass = "whitem" + ":" + "Pnot4u2";
             String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
             con.setRequestProperty("Authorization", basicAuth);*/
            con.connect();
           // uc.setRequestProperty("Authorization", basicAuth);
            //  InputStream in = uc.getInputStream();
            System.out.println("Connection opened Successfully");
            String readStream = readStream(con.getInputStream());
            Parser parser = new Parser();
            parser.setInputHTML(readStream);
            org.htmlparser.util.NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(ImageTag.class));
            System.out.println("Total Image Node List" + list.size());
            for (int i = 0; i < list.size(); i++) {
                ImageTag extracted = (ImageTag) list.elementAt(i);
                String extractedImageSrc = extracted.getImageURL();
                System.out.println("Image Source : " + extractedImageSrc);
                if (StringUtils.containsIgnoreCase(extractedImageSrc, "/FAB/STR.nsf/")) {
                    imageUrls.add(extractedImageSrc);
                }
            }

            for (String string : imageUrls) {
                System.out.println(string);
                System.out.println(uriLink.getPath());
                System.out.println(uriLink.getHost());
                downloadImage(uriLink.getProtocol() + "://" + uriLink.getHost() + string, "C:\\app\\");
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }

    }

    private static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {

            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void downloadImage(String sourceUrl, String targetDirectory) throws MalformedURLException, IOException, FileNotFoundException {
        URL imageUrl = new URL(sourceUrl);
        try (InputStream imageReader = new BufferedInputStream(imageUrl.openStream());
                OutputStream imageWriter = new BufferedOutputStream(new FileOutputStream(targetDirectory + File.separator
                                + FilenameUtils.getName(sourceUrl)));) {
            int readByte;

            while ((readByte = imageReader.read()) != -1) {
                imageWriter.write(readByte);
            }
        }
    }
}
