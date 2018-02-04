package pl.mysior;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connection {
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	Responder resp;
	Thread respThread;

	public String connect(String host, int port, boolean data) {
		String response = "";
		try {
			socket = new Socket(host, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			if (!data)
				response = readResponse();
		} catch (Exception ex) {
			//System.out.println(ex.toString());
		}
		return response;
	}

	public String readResponse() throws IOException {
		StringBuilder response = new StringBuilder();
		String line = null;

		while (!reader.ready())
			;// waiting for reader to be ready.
		while (reader.ready()) {
			if ((line = reader.readLine()) != null) {
				if (line.startsWith("-ERR") || (line.startsWith("5"))) {
					close();
					System.out.println(line);
					throw new RuntimeException("Server has returned an error: " + response);
				} else {
					response.append(line).append("\n");
				}
			}
		}
		//System.out.print(response.toString());
		return response.toString();
	}

	public String readDataResponse() throws IOException {
		StringBuilder response = new StringBuilder();
		String line = null;

		while (!reader.ready())
			;// waiting for reader to be ready.
		while (reader.ready()) {
			if ((line = reader.readLine()) != null) {
				response.append(line).append("\n");
			}
		}
		 System.out.print(response.toString());
		return response.toString();
	}

	public String sendCommand(String command) throws IOException {
		writer.write(command + "\r\n");
		writer.flush();
		return readResponse();
	}

	public void close() throws IOException {
		socket.close();
		reader.close();
		writer.close();

	}

	public boolean isConnected(Socket socket) {
		return socket.isConnected();
	}

	public Socket getSocket() {
		return this.socket;
	}

	public BufferedReader getReader() {
		return this.reader;
	}

	public BufferedWriter getWriter() {
		return this.writer;
	}
}
