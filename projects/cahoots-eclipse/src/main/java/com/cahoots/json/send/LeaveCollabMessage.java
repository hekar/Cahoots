package com.cahoots.json.send;

import com.cahoots.json.MessageBase;

public class LeaveCollabMessage extends MessageBase {

	private String username;
	private String opId;

	public LeaveCollabMessage(final String username, final String opid) {
		super("op", "leave");
		this.username = username;
		this.opId = opid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(final String opid) {
		this.opId = opid;
	}

}
