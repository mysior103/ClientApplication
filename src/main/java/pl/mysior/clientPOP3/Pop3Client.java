package pl.mysior.clientPOP3;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pl.mysior.Client;
import pl.mysior.Connection;

public class Pop3Client extends Client {
	private BufferedReader reader;
	private String host = "pop3.onet.pl";
	private String user = "test.123.2017@onet.pl";
	private String password = "Test1232017";
	private int port = 110;
	private Scanner s = new Scanner(System.in);
	Connection c = new Connection();

	public Pop3Client() throws IOException {
		this.host = command("host", this.host);
		this.port = command("port", this.port);
		c.connect(host, port, false);
		reader = c.getReader();
		login();
		System.out.println("Number of new emails: " + getNumberOfNewMessages());
		System.out.println("Insert the message number: ");
		int numOfMessage = s.nextInt();
		Message message = getMessage(numOfMessage);
		System.out.println(message.getHeader());
		System.out.println(message.getBody());
	}

	protected String readResponseLine() throws IOException {
		String response = reader.readLine();
		if (response.startsWith("-ERR"))
			throw new RuntimeException("Server has returned an error: " + response.replaceFirst("-ERR ", ""));
		return response;
	}

	public void login() throws IOException {
		this.user = command("user", this.user);
		this.password = command("password", this.password);
		c.sendCommand("USER " + user);
		c.sendCommand("PASS " + password);
	}

	public void logout() throws IOException {
		c.sendCommand("QUIT");
	}

	public int getNumberOfNewMessages() throws IOException {
		String response = c.sendCommand("LIST");
		String[] splitedResponse = response.split(" ");
		return Integer.parseInt(splitedResponse[1]);
	}

	private Message getMessage(int i) throws IOException {

		String response = c.sendCommand("RETR " + i);
		System.out.println(response);
		List<String> header = new ArrayList<String>();
		String[] headerElements = { "From: ", "Date: ", "Message-ID: ", "Subject: ", "To: " };
		while ((response = readResponseLine()).length() != 0) {
			for (String element : headerElements) {
				if (response.contains(element)) {
					if (element == "From: ") {
						if (response.contains("<")) {
							int lessThan = response.indexOf("<");
							header.add(response.substring(lessThan + 1, response.length() - 1));
						}
					} else {
						int colonPosition = response.indexOf(":");
						header.add(response.substring(colonPosition + 1, response.length()));
					}
				}
			}
		}
		StringBuilder body = new StringBuilder();
		while (!(response = readResponseLine()).equals(".")) {
			body.append(response + "\n");
		}
		return new Message(header, body.toString());
	}

	public List<Message> getMessages() throws IOException {
		int numOfMessages = getNumberOfNewMessages();
		List<Message> messageList = new ArrayList<Message>();
		for (int i = 1; i <= numOfMessages; i++) {
			messageList.add(getMessage(i));
		}
		return messageList;
	}

	public void disconnect() throws IOException {
		c.close();
	}
}
