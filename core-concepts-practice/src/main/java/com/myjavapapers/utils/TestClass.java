package com.myjavapapers.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestClass {
	public static void main(String[] args) throws IOException {

		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		System.out.println(dateFormat.format(date));
		File file = new File(dateFormat.format(date) + ".tsv") ;
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write("Writing to file");
		out.close();
	}
}
