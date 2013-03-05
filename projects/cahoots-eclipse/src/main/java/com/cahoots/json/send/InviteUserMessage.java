package com.cahoots.json.send;

import com.cahoots.json.MessageBase;

public class InviteUserMessage extends MessageBase {

	private String user;
	private String opId;

	public InviteUserMessage() {
		super("op", "invite");
	}

	public InviteUserMessage(final String user, final String opId) {
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
}
