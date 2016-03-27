package com.myjavapapers.io.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class PrintWriterDemo {
	
	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter printWriter = new PrintWriter(new File("C:/Users/Dileep/Desktop/temp/printWriter.txt"));
		print(printWriter, "revision", "1.0");
        print(printWriter, "action", "TRACK");
        print(printWriter, "lot", "10101");
        print(printWriter, "stage", "XGSDG");
        print(printWriter, "promisrecipe", "DSDSJFSS");
        print(printWriter, "actualqty", "0");
        print(printWriter, "autorecipe", "NA");
        print(printWriter, "equiptype", "DKSOSAOFAOSjf");
        print(printWriter, "equipment", "DJSDHSOHS");
        print(printWriter, "employee", "DSOHSOFHSHOF");
       // Long processTime = (long) (hist.processTime / 10);
        print(printWriter, "processtime", String.valueOf("ldsmpjgpdgds"));
        print(printWriter, "device ", "gegpegjpe");
        print(printWriter, "technology", "PROD");
        print(printWriter, "date ", new SimpleDateFormat("MM/dd/YYYY HH:mm:ss").format(new Date()));
        print(printWriter, "algorithm", "DSDJ)SIJ)I");
		printWriter.close();
	}
	
	public static void print(PrintWriter printWriter, String key,String value){
		printWriter.printf(key + StringUtils.repeat(" ", 17-key.length()) + value);
		printWriter.println();
		
	}

}
