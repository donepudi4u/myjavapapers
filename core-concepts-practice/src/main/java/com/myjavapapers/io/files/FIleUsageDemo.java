package com.myjavapapers.io.files;

import java.io.File;
import java.io.IOException;

public class FIleUsageDemo {
	
	public static void main(String[] args) throws IOException {
		File file = new File("C:/Users/Dileep/Desktop/temp/Dileep.txt");
		file.createNewFile();
		System.out.println(file.isDirectory());
	}

}
