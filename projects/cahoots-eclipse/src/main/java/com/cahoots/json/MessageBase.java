package com.cahoots.json;

public class MessageBase {

	private final String service;
	private final String type;

	protected MessageBase(String service, String type) {
		this.service = service;
		this.type = type;
	}

	public String getService() {
		return service;
	}

	public String getType() {
		return type;
	}

}
