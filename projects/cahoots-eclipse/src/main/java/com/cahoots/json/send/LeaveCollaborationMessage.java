package com.cahoots.json.send;

import com.cahoots.json.MessageBase;

public class LeaveCollaborationMessage extends MessageBase {

	private String user;
	private String opId;

	public LeaveCollaborationMessage(final String user, final String opid) {
		super("op", "leave");
		this.user = user;
		this.opId = opid;
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

	public void setOpId(final String opid) {
		this.opId = opid;
	}

}
