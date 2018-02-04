package pl.mysior;

import java.io.BufferedReader;
import java.io.IOException;


public class Responder implements Runnable {
	BufferedReader reader;
	private String resp;
	private boolean runB = true;
	
	public Responder(BufferedReader readerA) {
		this.reader = readerA;
	}

	@Override
	public void run() {
		while(runB) {
		try {
			setResp(readResponse());
		} catch (IOException e) {
			e.printStackTrace();
		}}
	}

	public String readResponse() throws IOException {
		StringBuilder response = new StringBuilder();
		String line = null;
		while (!reader.ready());
		if ((line = reader.readLine()) != null) {
			response.append(line).append("\n");
		}
		System.out.println("koniec");
		System.out.print(response.toString());
		return response.toString();
	}
	
	public boolean getRunB() {
		return runB;
	}

	public void setRunB(boolean runB) {
		this.runB = runB;
	}

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

}
