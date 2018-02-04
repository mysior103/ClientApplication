package pl.mysior.clientSMTP;

import pl.mysior.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

@SuppressWarnings("unused")
public class SmtpClient extends Client {
	private BufferedReader reader;
	private BufferedWriter writer;
	private String sender = "test.123.2017@onet.pl";
	private String recipient = "test.123.2017@onet.pl";
	private String host = "smtp.poczta.onet.pl";
	private String user = "test.123.2017@onet.pl";
	private String password = "Test1232017";
	private String response;
	private int port = 587;
	boolean debug = true;
	Scanner s = new Scanner(System.in);
	Connection c = new Connection();

	public SmtpClient() throws IOException {
		userInterface();
		handleMessage();
		boolean close = false;
		while (!close) {
			System.out.println("Send e-mail Again? (y/n)");
			String select = "";
			select = s.nextLine();
			char selectChar = select.charAt(0);

			if (selectChar == 'y') {
				handleMessage();
			} else if (selectChar == 'n') {
				close();
				close = true;
			} else {
				System.out.println("Not recognized!");
			}
		}
	}

	private void userInterface() throws IOException {
		setHostAndPort();
		c.connect(host, port, false);
		reader = c.getReader();
		writer = c.getWriter();
		authorize();
	}

	private void handleMessage() throws IOException {
		setSender();
		setRecipient();
		sendMessage();
	}

	private void greet(String name) throws IOException {
		String user = name.split("@")[0];
		String greet = "EHLO " + user;
		c.sendCommand(greet);
	}

	private void login() throws IOException {
		String userB64 = Base64.getEncoder().encodeToString(user.getBytes());
		String passwordB64 = Base64.getEncoder().encodeToString(password.getBytes());
		c.sendCommand("AUTH LOGIN");
		c.sendCommand(userB64);
		response = c.sendCommand(passwordB64);
		if (response.startsWith("235"))
			System.out.println("Authorization succesfully!");
	}

	private void setSender() throws IOException {
		this.sender = command("sender", this.sender);
		c.sendCommand("MAIL FROM:<" + this.sender + ">");
	}

	private void setRecipient() throws IOException {
		this.recipient = command("recipient", this.recipient);
		c.sendCommand("RCPT TO:<" + this.recipient + ">");
	}

	private void authorize() throws IOException {
		System.out.println("Authorization:");
		this.user = command("user", this.user);
		this.password = command("password", this.password);
		greet(user);
		login();
	}

	private void setHostAndPort() {
		this.host = command("host", this.host);
		this.port = command("port", this.port);
	}

	private void sendMessage() throws IOException {
		c.sendCommand("DATA");
		System.out.println("Insert your subject: ");
		String subject = s.nextLine();
		System.out.println("Insert your message: ");
		String message = s.nextLine();
		String readyToSend = "Subject: " + subject + "\n" + message + "\r\n.";
		c.sendCommand(readyToSend);
		System.out.println("E-mail sent!");
	}

	private void close() throws IOException {
		c.sendCommand("QUIT");
		c.close();
		System.out.println("Disconnected!");
	}
}
