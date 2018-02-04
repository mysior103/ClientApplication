package pl.mysior.clientPOP3;

import java.util.List;

public class Message {
	private List<String> header;
	private String body;

	protected Message(List<String> header, String body) {
		this.header = header;
		this.body = body;
	}

	public List<String> getHeader() {
		return header;
	}

	public String getBody() {
		return body;
	}
}
