package com.cahoots.json.send;

import java.util.Date;

import com.cahoots.json.MessageBase;

public class ChatSendMessage extends MessageBase {
	private String from;
	private String to;
	private Date date;
	private String message;
	
	public ChatSendMessage(){
		super("chat", "send");
	}
	
	public ChatSendMessage(String from, String to, Date date, String message)
	{
		super("chat", "send");
		this.from = from;
		this.to = to;
		this.date = date;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
