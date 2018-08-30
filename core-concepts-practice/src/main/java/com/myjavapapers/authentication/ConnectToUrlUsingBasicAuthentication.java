package com.myjavapapers.authentication;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.ParserException;

public class ConnectToUrlUsingBasicAuthentication {

	public static void main(String[] args) throws ParserException {

		try {
			String webPage = "http://10.32.8.22/__88256801007AD027.nsf/0/044315BF610AAE6E8825689D0003A73D?OpenDocument";
			String name = "whitem";
			String password = "Pnot4u2";
			 Collection<String> imageUrls = new ArrayList<String>();
			 
			String authString = name + ":" + password;
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: " + authStringEnc);

			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic "
					+ authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String result = sb.toString();

			System.out.println("*** BEGIN ***");
			System.out.println(result);
			System.out.println("*** END ***");
			
			Parser parser = new Parser();
            parser.setInputHTML(result);
            org.htmlparser.util.NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(ImageTag.class));
            System.out.println("Total Image Node List" + list.size());
            for (int i = 0; i < list.size(); i++) {
                ImageTag extracted = (ImageTag) list.elementAt(i);
                String extractedImageSrc = extracted.getImageURL();
                System.out.println("Image Source : " + extractedImageSrc);
               // if (StringUtils.containsIgnoreCase(extractedImageSrc, "/FAB/STR.nsf/")) {
                    imageUrls.add(extractedImageSrc);
              //  }
            }
int i =0 ;
            for (String string : imageUrls) {
                System.out.println(string);
                System.out.println(url.getPath());
                System.out.println(url.getHost());
                //downloadImage(url.getProtocol() + "://" + url.getHost() + string, "C:\\temp\\");
                downloadImage(string, "C:\\Users\\Dileep\\Desktop\\temp",i++);
            }
			
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 public static void downloadImage(String sourceUrl, String targetDirectory,int i) throws MalformedURLException, IOException, FileNotFoundException {
		 if (!sourceUrl.contains("https")){
			 sourceUrl = "https://github.com/"+sourceUrl;
		 }
	        URL imageUrl = new URL(sourceUrl);
	        try (InputStream imageReader = new BufferedInputStream(imageUrl.openStream());
	                OutputStream imageWriter = new BufferedOutputStream(new FileOutputStream(targetDirectory +"image"+i+".jpg") )) {
	            int readByte;

	            while ((readByte = imageReader.read()) != -1) {
	                imageWriter.write(readByte);
	            }
	        }
	    }
	 
	 public static void downloadImage(String sourceUrl, String targetDirectory) throws MalformedURLException, IOException, FileNotFoundException {
//	        URL imageUrl = new URL(sourceUrl);
	        HttpURLConnection con = authenticateAndGetConnectionObject(sourceUrl);
	         //   String readStream = readStream(con.getInputStream());
	        BufferedImage read = ImageIO.read(con.getInputStream());
	        ImageIO.write(read, "jpg", new File("c:\\app\\image1.jpg"));
	       
	    }
	private static HttpURLConnection authenticateAndGetConnectionObject(String url )throws MalformedURLException, IOException {
		URL imageUrl = new URL(url);
		String authString = "whitem:Pnot4u2";
		    System.out.println("auth string: " + authString);
		    byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		    String authStringEnc = new String(authEncBytes);
		    System.out.println("Base64 encoded auth string: " + authStringEnc);
		    HttpURLConnection con = (HttpURLConnection) imageUrl.openConnection();
		    con.setRequestProperty("Authorization", "Basic "+ authStringEnc);
		    con.connect();
		    System.out.println("Connection opened Successfully");
		return con;
	}

	
	public static void downloadImage(String protocal, String host ,String sourceUrl, String targetDirectory) throws MalformedURLException, IOException, FileNotFoundException {
        String imageURL = buildImageURL(protocal, host, sourceUrl);
        //HttpURLConnection urlConnection = authenticateAndGetConnectionObject("http://10.32.8.22/FAB/STR.nsf/0/044315bf610aae6e8825689d0003a73d/STR_INSTRUCTION/0.D84.jpg");
        HttpURLConnection urlConnection = authenticateAndGetConnectionObject(imageURL);
        BufferedImage read = ImageIO.read(urlConnection.getInputStream());
        ImageIO.write(read, "jpg", new File(targetDirectory + "\\image1.jpg"));
    }
	private static String buildImageURL(String protocal, String host,
			String sourceUrl) throws MalformedURLException {
		URL imageUrl = new URL(protocal+"://"+ host+"/"+sourceUrl);

        System.out.println("Image path [ " + imageUrl.getPath() + " ]");
        System.out.println("Image Query params [ " + imageUrl.getQuery() + " ]");
        String query = imageUrl.getQuery();

        String[] imageFormat = query.split("=");
        if (imageFormat != null && imageFormat.length > 0) {
            System.out.println("Image Format " + imageFormat[1]);
        }
        String imagePath = imageUrl.getPath();
        String imageName = imagePath.substring(StringUtils.lastIndexOf(imagePath, "/"), imagePath.length());
        String newPath = imagePath.substring(1,StringUtils.lastIndexOf(imagePath, "/")) + imageName+"."+imageFormat[1];
        System.out.println("New Path" + newPath);
        String imageURL = protocal + "://"+host+newPath;
        System.out.println("New URL Built : " + imageURL);
		return imageURL;
	}
}