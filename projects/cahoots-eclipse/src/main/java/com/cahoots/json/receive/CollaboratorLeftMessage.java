package com.cahoots.json.receive;

import com.cahoots.serialize.json.MessageBase;

public class CollaboratorLeftMessage extends MessageBase {

	public CollaboratorLeftMessage() {
		super("op", "left");
	}

	public CollaboratorLeftMessage(final String service, final String type) {
		super(service, type);
	}

	private String user;

	private String opId;

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(final String opId) {
		this.opId = opId;
	}
}
