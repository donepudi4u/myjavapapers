package com.myjavapapers.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class ServerToServerFTPTransfer {
	
	public static void main(String[] args) throws FileNotFoundException {
		FTPClient remoteOneClient = getFTPClient("localhost", 16885, "dileep", "7u8i9o0p");
		FTPClient remoteTwoClient = getFTPClient("localhost", 16885, "bhavani", "1q2w3e4r");
		String file1 = "C:/Users/Dileep/Desktop/temp/mavenLog.txt";  // C:\Users\Dileep\Desktop\temp
		String file2 = "C:/Users/Dileep/Desktop/bhavani/newMaven.txt";
		ProtocolCommandListener listener;
		 listener = new PrintCommandListener(new PrintWriter(new File(file2)), true);
		 remoteOneClient.addProtocolCommandListener(listener);
		 remoteTwoClient.addProtocolCommandListener(listener);
		try {
			remoteTwoClient.enterRemotePassiveMode();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			remoteOneClient.enterRemoteActiveMode(InetAddress.getByName(remoteTwoClient.getPassiveHost()),remoteTwoClient.getPassivePort());
			if (remoteOneClient.remoteRetrieve(file1) && remoteTwoClient.remoteStoreUnique(file2))
            {
                // if(ftp1.remoteRetrieve(file1) && ftp2.remoteStore(file2)) {
                // We have to fetch the positive completion reply.
				remoteOneClient.completePendingCommand();
				remoteTwoClient.completePendingCommand();
            }
            else
            {
                System.err.println("Couldn't initiate transfer.  Check that filenames are valid.");
            }
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}
	
	private static FTPClient getFTPClient(String hostname ,int port , String user,String password) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(hostname, port);
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Operation failed. Server reply code: "+ replyCode);
			}
			 if (!ftpClient.login(user, password))
	            {
	                System.err.println("Could not login to " + hostname);
	            }else {
	            	System.out.println("Logged in success");
	            }
			
		} catch (SocketException e) {
			 if (ftpClient.isConnected())
	            {
	                try
	                {
	                	ftpClient.disconnect();
	                }
	                catch (IOException f)
	                {
	                	 e.printStackTrace();
	                }
	            }
	            System.err.println("Could not connect to server.");
			e.printStackTrace();
		} catch (IOException e) {

            if (ftpClient.isConnected())
            {
                try
                {
                	ftpClient.disconnect();
                }
                catch (IOException f)
                {
                	 e.printStackTrace();
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
        
		}
		return ftpClient;
	}

}
