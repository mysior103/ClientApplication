package pl.mysior;

import pl.mysior.clientSMTP.*;
import pl.mysior.clientPOP3.*;
import java.io.IOException;
import java.util.Scanner;

import pl.mysior.clientFTP.FtpClient;

public class App {
	FtpClient ftp;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		boolean exit = false;
		while (!exit) {
			Scanner s = new Scanner(System.in);

			System.out.println("Select client type: \n1. SMTP client\n2. POP3 client\n3. FTP client\n4. close");
			int type = s.nextInt();
			try {
				switch (type) {
				case 1:
					SmtpClient smpt = new SmtpClient();
					break;
				case 2:
					Pop3Client pop3 = new Pop3Client();
					break;
				case 3:
					FtpClient ftp = new FtpClient();
					break;
				case 4:
					exit = true;
					System.out.println("App closed");
					s.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
}
