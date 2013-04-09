package com.cahoots.json.send;

import com.cahoots.serialize.json.MessageBase;

public class InviteUserMessage extends MessageBase {

	private String user;
	private String opId;
	private String sharer;

	public String getSharer() {
		return sharer;
	}

	public void setSharer(final String sharer) {
		this.sharer = sharer;
	}

	public InviteUserMessage() {
		super("op", "invite");
	}

	public InviteUserMessage(final String sharer, final String user,
			final String opId) {
		this();
		this.sharer = sharer;
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
