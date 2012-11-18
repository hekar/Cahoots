package com.cahoots.events;

import com.cahoots.json.Collaborator;

public class UserChangeEvent {
	
	private Collaborator user;
	
	public UserChangeEvent(Collaborator user)
	{
		this.user = user;
	}
	
	public Collaborator getUser()
	{
		return user;
	}
}
