package com.cahoots.json.receive;

import com.cahoots.json.Collaborator;
import com.cahoots.json.MessageBase;

public class UserChangeMessage extends MessageBase {
	public UserChangeMessage(){}
	
	public UserChangeMessage(Collaborator user)
	{
		this.user = user;
		super.service = "users";
		super.type = "status";
	}
	
	public Collaborator user;
}
