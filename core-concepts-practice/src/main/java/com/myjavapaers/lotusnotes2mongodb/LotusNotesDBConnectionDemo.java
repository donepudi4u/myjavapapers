package com.myjavapaers.lotusnotes2mongodb;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Document;

public class LotusNotesDBConnectionDemo {

	public static void main(String[] args) {
	
		
	}
	
	public static void getHTMLConverter(Database thisdb, Document sourceDoc, String fieldName){
		
		try {
		Document createDocument = thisdb.createDocument();
			URL docURL = new URL(createDocument.getHttpURL());
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotesException e) {
			e.printStackTrace();
		}
		
	}

}
