package com.cahoots.connection.serialize.send;

import com.cahoots.connection.serialize.MessageBase;

public class JoinCollaborationMessage extends MessageBase {
	public JoinCollaborationMessage() {
		super("op", "join");
	}

	public JoinCollaborationMessage(final String user, final String opId) {
		this();
		this.user = user;
		this.opId = opId;
	}

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

	private String user;

	private String opId;
}
