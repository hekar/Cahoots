package com.cahoots.json.receive;

import com.cahoots.json.Collaborator;
import com.cahoots.json.MessageBase;

public class UserListMessage extends MessageBase {
	private Collaborator[] users;

	public UserListMessage() {
		// TODO: Make sure this is actually the correct service/type
		super("users", "list");
	}

	public Collaborator[] getUsers() {
		return users;
	}

	public void setUsers(Collaborator[] users) {
		this.users = users;
	}

}