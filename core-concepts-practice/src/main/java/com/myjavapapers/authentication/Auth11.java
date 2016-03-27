package com.myjavapapers.authentication;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Auth11 extends Frame implements ActionListener {

	private TextField tf = new TextField();
	private TextArea ta = new TextArea();

	public void actionPerformed(ActionEvent e) {
		String s = tf.getText();
		if (s.length() != 0)
			ta.setText(fetchURL(s));
	}

	public Auth11() {

		super("URL11 Password");

		// Setup screen
		add(tf, BorderLayout.NORTH);
		ta.setEditable(false);
		add(ta, BorderLayout.CENTER);
		tf.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	@SuppressWarnings("restriction")
	private String fetchURL(String urlString) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		try {
			URL url = new URL(urlString);

			// Popup Window to request username/password password
			MyAuthenticator ma = new MyAuthenticator();
			String userPassword = ma.getPasswordAuthentication(this,
					url.getHost());

			// Encode String
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword
					.getBytes());

			// or
			// String encoding = Base64Converter.encode
			// (userPassword.getBytes());

			// Need to work with URLConnection to set request property
			URLConnection uc = url.openConnection();
			uc.setRequestProperty("Authorization", "Basic " + encoding);
			InputStream content = (InputStream) uc.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					content));
			String line;
			while ((line = in.readLine()) != null) {
				pw.println(line);
			}
		} catch (MalformedURLException e) {
			pw.println("Invalid URL");
		} catch (IOException e) {
			pw.println("Error reading URL");
		}
		return sw.toString();
	}

	public static void main(String args[]) {
		Frame f = new Auth11();
		f.setSize(300, 300);
		f.setVisible(true);
	}

	class MyAuthenticator {
		String getPasswordAuthentication(Frame f, String prompt) {
			final Dialog jd = new Dialog(f, "Enter password", true);
			jd.setLayout(new GridLayout(0, 1));
			Label jl = new Label(prompt);
			jd.add(jl);
			TextField username = new TextField();
			username.setBackground(Color.lightGray);
			jd.add(username);
			TextField password = new TextField();
			password.setEchoChar('*');
			password.setBackground(Color.lightGray);
			jd.add(password);
			Button jb = new Button("OK");
			jd.add(jb);
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jd.dispose();
				}
			});
			jd.pack();
			jd.setVisible(true);
			return username.getText() + ":" + password.getText();
		}
	}

}