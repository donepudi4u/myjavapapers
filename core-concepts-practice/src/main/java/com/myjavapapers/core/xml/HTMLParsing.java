package com.myjavapapers.core.xml;

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
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.util.Base64;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.xml.sax.SAXException;

public class HTMLParsing {

	public static void main(String[] args) throws SAXException, IOException,
			ParserConfigurationException {
		//FirstApproch();
		secondApproch();
		/*
		 * DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 * DocumentBuilder db = dbf.newDocumentBuilder(); Document document =
		 * db.parse(new InputSource(new
		 * StringReader(FileUtils.readFileToString(new
		 * File("C:\\Users\\Dileep\\Desktop\\test.html"))))); Element element =
		 * document.getElementById("img"); if (element != null) { if
		 * (element.getNodeType() == Element.ELEMENT_NODE) { NamedNodeMap
		 * attributes = element.getAttributes(); Node namedItem =
		 * attributes.getNamedItem("src");
		 * System.out.println(namedItem.getTextContent()); } }
		 */
	}

	/*public static void FirstApproch() throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new InputSource(new StringReader(FileUtils
				.readFileToString(new File(
						"C:\\Users\\Dileep\\Desktop\\test.html")))));
		NodeList imgs = document.getElementsByTagName("img");
		List<String> srcs = new ArrayList<String>();

		for (int i = 0; i < imgs.getLength(); i++) {
			srcs.add(imgs.item(i).getAttributes().getNamedItem("src")
					.getNodeValue());
		}

		for (String src : srcs) {
			System.out.println(src);
		}
	}*/

	public static void secondApproch() {
		Collection<String> imageUrls = new ArrayList<String>();

		try {

			URL uriLink = new URL("http://www.google.com");
			HttpURLConnection con = (HttpURLConnection) uriLink.openConnection();
			Authenticator.setDefault (new Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication ("username", "password".toCharArray());
			    }
			});
			
			URLConnection uc = uriLink.openConnection();
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
			uc.setRequestProperty ("Authorization", basicAuth);
			InputStream in = uc.getInputStream();
			
		      String readStream = readStream(con.getInputStream());
			Parser parser = new Parser();
			 parser.setInputHTML(readStream);
			NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(ImageTag.class));

			for (int i = 0; i < list.size(); i++) {
				ImageTag extracted = (ImageTag) list.elementAt(i);
				String extractedImageSrc = extracted.getImageURL();
				imageUrls.add(extractedImageSrc);
			}

			for (String string : imageUrls) {
				System.out.println(string);
				System.out.println(uriLink.getPath());
				System.out.println(uriLink.getHost());
				downloadImage(uriLink.getProtocol()+"://"+uriLink.getHost()+string, "C:\\Users\\Dileep\\Desktop\\");
			}
		} catch (Exception e) {

			e.printStackTrace();
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
	
	public static void downloadImage(String sourceUrl, String targetDirectory)throws MalformedURLException, IOException, FileNotFoundException
    {
        URL imageUrl = new URL(sourceUrl);
        try (InputStream imageReader = new BufferedInputStream(imageUrl.openStream());
                OutputStream imageWriter = new BufferedOutputStream(new FileOutputStream(targetDirectory + File.separator
                                + FilenameUtils.getName(sourceUrl)));)
        {
            int readByte;

            while ((readByte = imageReader.read()) != -1)
            {
                imageWriter.write(readByte);
            }
        }
    }
}
