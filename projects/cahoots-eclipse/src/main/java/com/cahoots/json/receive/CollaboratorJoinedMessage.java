package com.cahoots.json.receive;

import com.cahoots.json.MessageBase;

public class CollaboratorJoinedMessage extends MessageBase {

	private String opId;

	private String user;

	public String getOpId() {
		return opId;
	}

	public void setOpId(final String opId) {
		this.opId = opId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public CollaboratorJoinedMessage() {
		super("op", "joined");
	}

	public CollaboratorJoinedMessage(final String user, final String opId) {
		this();
		this.user = user;
		this.opId = opId;
	}
}
