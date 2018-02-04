package pl.mysior.clientFTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.mysior.Client;
import pl.mysior.Connection;

public class FtpClient extends Client {
	@SuppressWarnings("unused")
	private BufferedReader controlReader;
	@SuppressWarnings("unused")
	private BufferedWriter writer;
	@SuppressWarnings("unused")
	private BufferedReader dataReader;
	private String host = "demo.wftpserver.com";
	private String user = "demo-user";
	private String password = "demo-user";
	private int controlPort = 21;
	boolean debug = false;
	private String response = "";
	private String workingDir = "/";
	private Scanner s = new Scanner(System.in);
	private Connection c = new Connection();
	private Connection dataConnection = new Connection();

	// Constructors:

	public FtpClient() throws IOException {
		userInterface();
	}

	private String readInput() {
		return s.nextLine();
	}

	// UI:
	private void userInterface() throws IOException {
		boolean quit = false;
		System.out.println("Welcome to FTP CLient!");
		this.host = command("host", this.host);
		this.controlPort = command("port", this.controlPort);

		c.connect(host, controlPort, false);
		controlReader = c.getReader();
		writer = c.getWriter();

		login();
		config();
		workingDir = getWorkingDir();
		System.out.println(help());

		while (!quit) {
			System.out.print(workingDir + ">");
			String command = readInput();
			if (command.contains("cd")) {
				try {
					String cat = command.split(" ", 2)[1];
					setWorkingDir(cat);

				} catch (Exception e) {
					System.out.println("Command unrecognized !");
				}
			} else {
				switch (command) {
				case "pwd":
					System.out.println(getWorkingDir());
					break;
				case "ls":
					System.out.println(showFileList());
					break;
				case "ls -la":
					System.out.println(showDetailedFileList());
					break;
				case "help":
					System.out.println(help());
					break;
				case "quit":
					System.out.println(quit());
					quit = true;
					break;
				default:
					System.out.println("Command unrecognized!");
				}
			}
		}
	}

	private String quit() throws IOException {
		response = c.sendCommand("QUIT");
		c.close();
		return "Server disconnected!";
	}

	private void login() throws IOException {
		String responseCode = "230";
		while (responseCode == "230") {
			this.user = command("user", this.user);
			c.sendCommand("USER " + user);
			this.password = command("password", this.password);
			response = c.sendCommand("PASS " + this.password);
			responseCode = response.split(" ")[0];
		}
		System.out.println("User logged in correctly!");
	}

	private void config() throws IOException { // setting type ASCII
		c.sendCommand("TYPE A");
	}

	private void dataConnect() throws IOException {
		int port = getPasivePort();
		dataConnection.connect(host, port, true);
		dataReader = dataConnection.getReader();
	}
	
	private String getWorkingDir() throws IOException {
		response = c.sendCommand("PWD");
		String dir = response.split(" ")[1];
		dir = dir.replace("\"", "");
		workingDir = dir;
		return dir;
	}

	private void setWorkingDir(String dir) throws IOException {
		response = c.sendCommand("CWD " + dir);
		if (response.startsWith("550")) {
			System.out.println("Directory not found");
		} else {
			getWorkingDir();
		}
	}

	private int getPasivePort() throws IOException {
		String ip = "";
		response = c.sendCommand("PASV");
		Matcher m = Pattern.compile("\\((.*?)\\)").matcher(response);
		while (m.find()) {
			ip = m.group(1);
		}
		String[] splitedIP = ip.split(",");
		int p1 = Integer.parseInt(splitedIP[4]);
		int p2 = Integer.parseInt(splitedIP[5]);
		return countPort(p1, p2);
	}

	private int countPort(int p1, int p2) {
		return p1 * 256 + p2;
	}

	private String showFileList() throws IOException {// NLST
		dataConnect();
		c.sendCommand("NLST");
		response = dataConnection.readDataResponse();
		c.readResponse();
		return response;
	}

	private String showDetailedFileList() throws IOException {
		dataConnect();
		c.sendCommand("LIST");
		response = dataConnection.readDataResponse();
		c.readResponse();
		return response;
	}

	private String help() {
		String help = "Avaliable commads: \n" + "ls : list the names of the files in the current directory\n" // NLST
				+ "ls -la : detailed list the names of the files in the current directory\n" // LIST
				+ "pwd : show current directory\n" // PWD
				+ "cd [dir] : go to directory\n" // CWD
				+ "quit : disconnect\n"; // QUIT
		return help;
	}
}
