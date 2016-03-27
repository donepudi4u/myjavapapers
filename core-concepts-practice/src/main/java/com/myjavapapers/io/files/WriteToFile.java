package com.myjavapapers.io.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class WriteToFile {
	
	public static void main(String[] args) throws IOException {
		File file = new File("C:/Users/Dileep/GDE/Avinash Project Work/Caifornia/IDSDataWriteTofiles/Hello1.txt");
		List<String> lines = new ArrayList<String>();
		lines.add("Name \t Dileep");
		lines.add("Age \t 30");
		lines.add("Place \t hyderabad");
		FileUtils.writeLines(file, lines);
		File file2 = new File("C:/Users/Dileep/GDE/Avinash Project Work/Caifornia/IDSDataWriteTofiles/Hello2.swm");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Name \t      Dileep \n");
		stringBuilder.append("Age \t       30 \n");
		stringBuilder.append("Last Name \t Donepudi \n");
		stringBuilder.append("Place \t     Hyderabad \n");
		
		FileUtils.writeStringToFile(file2, stringBuilder.toString());
		
		
	}

}
