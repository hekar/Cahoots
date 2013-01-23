package com.cahoots.json.receive;

import java.util.Date;

import com.cahoots.json.MessageBase;

public class ChatReceiveMessage extends MessageBase {
	private String from;
	private String message;
	private Date date;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
