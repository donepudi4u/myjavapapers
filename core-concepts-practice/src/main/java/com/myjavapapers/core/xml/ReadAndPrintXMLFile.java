package com.myjavapapers.core.xml;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReadAndPrintXMLFile{

    public static void main (String argv []){
    try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("C:\\Users\\Dileep\\Desktop\\test.xml"));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());


            NodeList listOfNotesBitMaps = doc.getElementsByTagName("notesbitmap");
            int totalBitMaps = listOfNotesBitMaps.getLength();
            System.out.println("Total no of notes Bit maps  : " + totalBitMaps);

            for(int s=0; s<listOfNotesBitMaps.getLength() ; s++){


                Node firstPersonNode = listOfNotesBitMaps.item(s);
                System.out.println("******************* " + s + "********************** START");
                if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){
                	Element firstPersonElement = (Element)firstPersonNode;
                	System.out.println(firstPersonElement.getTextContent());
                	//byte[] bytes = firstPersonElement.getTextContent().getBytes();
                	byte[] imageByteArray = decodeImage(firstPersonElement.getTextContent());

        			// Write a image byte array into file system
        			FileOutputStream imageOutFile = new FileOutputStream("C:\\Users\\Dileep\\Desktop\\"+ s +".bmp");

        			imageOutFile.write(imageByteArray);

        			imageOutFile.close();

        			System.out.println("Image Successfully Manipulated!");
                	
                }
               System.out.println("#################### " + s+ "######################## END ");
                /*if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstPersonElement = (Element)firstPersonNode;

                    //-------
                    NodeList firstNameList = firstPersonElement.getElementsByTagName("first");
                    Element firstNameElement = (Element)firstNameList.item(0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                    System.out.println("First Name : " + ((Node)textFNList.item(0)).getNodeValue().trim());

                    //-------
                    NodeList lastNameList = firstPersonElement.getElementsByTagName("last");
                    Element lastNameElement = (Element)lastNameList.item(0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                    System.out.println("Last Name : " + ((Node)textLNList.item(0)).getNodeValue().trim());

                    //----
                    NodeList ageList = firstPersonElement.getElementsByTagName("age");
                    Element ageElement = (Element)ageList.item(0);

                    NodeList textAgeList = ageElement.getChildNodes();
                    System.out.println("Age : " + ((Node)textAgeList.item(0)).getNodeValue().trim());

                    //------


                }//end of if clause

              */
            }//end of for loop with s var


        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
        //System.exit (0);

    }//end of main

    /**
     * Decodes the base64 string into byte array
     *
     * @param imageDataString - a {@link java.lang.String}
     * @return byte array
     */
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }

}
