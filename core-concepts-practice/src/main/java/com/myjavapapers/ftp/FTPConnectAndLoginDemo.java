package com.myjavapapers.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.spi.FileTypeDetector;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPConnectAndLoginDemo {
	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
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
			//ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			showServerReply(ftpClient);
			if (!success) {
				System.out.println("Could not login to the server");
				return;
			} else {
				System.out.println("LOGGED IN SERVER");
				ftpClient.changeWorkingDirectory("C:\\Users\\Dileep\\Desktop\\temp");
				FTPFile[] listFiles = ftpClient.listFiles();
				System.out.println("Number Of FIles : "+listFiles.length);
			//	for (FTPFile ftpFile : listFiles) {
				//		if (ftpFile.getName().startsWith("Dileep")){
							System.out.println("File with DIleep Found");
							// (ftpFile.isDirectory() || ftpClient.makeDirectory("newFolder")){
								File outFile = new File("C:\\Users\\Dileep\\Desktop\\temp\\newFolder","NewFile.txt");
								ftpClient.enterLocalPassiveMode();
								FileOutputStream outputStream = new FileOutputStream(outFile);
							//	OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outFile));
								if (ftpClient.retrieveFile("Dileep.txt", outputStream)){
									//outputStream.flush();
									outputStream.close();
								}
								//outputStream.write(b);
							//	FileInputStream inputStream = new FileInputStream(outFile);//new File("C:/Users/Dileep/GDE/temp/Dileep.txt"));
							//	if (ftpClient.makeDirectory("C:\\Users\\Dileep\\Desktop\\temp\\NewFolder")){
								//	ftpClient.enterLocalPassiveMode();
								//	ftpClient.storeFile("mavenLog.txt",inputStream);
								//}
						//	}
				//	}
					//System.out.println(ftpFile.getName());
				//}
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