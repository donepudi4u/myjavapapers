package com.myjavapaers.lotusnotes2mongodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;

import com.myjavapapers.authentication.MyAuthenticator;

public class HttpClientDemo {

	public static void main(String[] args) throws IOException {
		String link = "https://drive.google.com/open?id=0BxDMiIfBYQR9SjlKR2xnYUZLUWc";
		InputStream is = null;
        BufferedReader br;
        String line;
		//CloseableHttpClient httpClient = HttpClients.createDefault();
		// Install Authenticator
		MyAuthenticator.setPasswordAuthentication("donepudi4u", "D@ne9ud11985");
		Authenticator.setDefault (new MyAuthenticator ());
	//	HttpGet get = new HttpGet(link);
		// response = httpClient.execute(get);

		try {
			URL url = new URL(link);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (MalformedURLException mue) {
             mue.printStackTrace();
        } catch (IOException ioe) {
             ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
            	System.out.println(ioe);
            }
        }
	}
}
