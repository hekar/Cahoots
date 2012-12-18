package com.cahoots.json.receive;

import com.cahoots.json.Collaborator;
import com.cahoots.json.MessageBase;

public class UserListMessage extends MessageBase {
	public UserListMessage(){}
	
	public Collaborator[] users;
}
