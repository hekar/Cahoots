package com.cahoots.serialize.json.send;

import com.cahoots.serialize.json.MessageBase;

public class ChatSendMessage extends MessageBase {
	private String from;
	private String to;
	private String timestamp;
	private String message;

	public ChatSendMessage() {
		super("chat", "send");
	}

	public ChatSendMessage(String from, String to, String date, String message) {
		super("chat", "send");
		this.from = from;
		this.to = to;
		this.timestamp = date;
		this.message = message;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String date) {
		this.timestamp = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
