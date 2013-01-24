package com.cahoots.json.receive;

import com.cahoots.json.Collaborator;
import com.cahoots.json.MessageBase;

public class UserChangeMessage extends MessageBase {
	private Collaborator user;

	public UserChangeMessage() {
		this(null);
	}

	public UserChangeMessage(Collaborator user) {
		super("users", "status");
		this.user = user;
	}

	public Collaborator getUser() {
		return user;
	}

	public void setUser(Collaborator user) {
		this.user = user;
	}

}
