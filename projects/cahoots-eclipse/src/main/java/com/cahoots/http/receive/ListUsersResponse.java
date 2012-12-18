package com.cahoots.http.receive;

import java.util.ArrayList;
import java.util.List;

public class ListUsersResponse {
	private List<String> users = new ArrayList<String>();

	public ListUsersResponse() {
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(final List<String> users) {
		this.users = users;
	}

}
