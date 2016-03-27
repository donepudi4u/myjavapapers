package com.myjavapapers.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.spi.FileTypeDetector;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.StreamUtils;

public class SEMVisionTransferResults {
	
		private static void showServerReply(FTPClient ftpClient) {
			String[] replies = ftpClient.getReplyStrings();
			if (replies != null && replies.length > 0) {
				for (String aReply : replies) {
					System.out.println("SERVER: " + aReply);
				}
			}
		}
		
		public static void manipulateFile(InputStream fileInputStream) throws IOException{
			//FileReader fr = new FileReader("infile.txt"); 
		//	BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream)); 
		//	FileWriter fw = new FileWriter(new File("C:\\Users\\Dileep\\Desktop\\temp\\newFolder","outFile.txt")); 
		//	String line;
			FileUtils.copyInputStreamToFile(fileInputStream, new File("C:\\Users\\Dileep\\Desktop\\temp\\newFolder","outFile.txt"));
			List<String> fileToString = FileUtils.readLines(new File("C:\\Users\\Dileep\\Desktop\\temp\\newFolder","outFile.txt"));
			System.out.println("************************************");
			for (String line : fileToString) {
				System.out.println(line);
				line = line.trim(); // remove leading and trailing whitespace
				line = line.replaceAll("\t+"," ");//StringUtils.replace(line, "  ", " ");
				line = line.replaceAll("  "," ");//StringUtils.replace(line, "  ", " ");
				if (StringUtils.isNotBlank(line)) // don't write out blank lines
				{
					String[] split = StringUtils.split(line," ");
					if (split != null && split.length >=5 ){
						String intVal = split[5];
						if (StringUtils.isNotBlank(intVal)&& StringUtils.isNumeric(intVal)){
							Float valeue = Float.valueOf(intVal);
							Float value2 = valeue/10;
							split[5] = value2.toString();
							line = split.toString();
						}
					}
				}
				FileUtils.writeStringToFile(new File("C:\\Users\\Dileep\\Desktop\\temp\\newFolder","outFile.txt"), line, true);
			}
			System.out.println("************************************");			
			/*while((line = br.readLine()) != null)
			{ 
				line = line.trim(); // remove leading and trailing whitespace
				line = line.replaceAll("\\s+"," ");//StringUtils.replace(line, "  ", " ");
				if (StringUtils.isNotBlank(line)) // don't write out blank lines
				{
					fw.write(line, 0, line.length());
				}
			} */
			fileInputStream.close();
			//fw.close();
			
		}

		public static void main(String[] args) {
			String server = "localhost";
			int port = 16885;
			String user = "dileep";
			String pass = "7u8i9o0p";
			FTPClient ftpClient = new FTPClient();
			try {
				ftpClient.connect(server, port);
				showServerReply(ftpClient);
				int replyCode = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(replyCode)) {
					System.out.println("Operation failed. Server reply code: "
							+ replyCode);
					return;
				}
				boolean success = ftpClient.login(user, pass);
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				showServerReply(ftpClient);
				if (!success) {
					System.out.println("Could not login to the server");
					return;
				} else {
					System.out.println("LOGGED IN SERVER");
					ftpClient.changeWorkingDirectory("C:\\Users\\Dileep\\Desktop\\temp");
					//File outFile = new File("C:\\Users\\Dileep\\Desktop\\temp\\newFolder","NewFile.txt");
					ftpClient.enterLocalPassiveMode();
					//FileOutputStream outputStream = new FileOutputStream(outFile);
					InputStream fileInputStream = ftpClient.retrieveFileStream("Dileep.txt");
					manipulateFile(fileInputStream);
					/*if (ftpClient.retrieveFile("Dileep.txt", outputStream)){
						outputStream.close();
					}*/
				}
				ftpClient.logout();
			} catch (IOException ex) {
				System.out.println("Oops! Something wrong happened");
				ex.printStackTrace();
			}finally{
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
