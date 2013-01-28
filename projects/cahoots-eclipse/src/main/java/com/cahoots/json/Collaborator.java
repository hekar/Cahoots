package com.cahoots.json;

import com.cahoots.chat.CollaboratorStatus;

public class Collaborator {

	private String name;
	private String role;
	private String status;
	private String username;

	public Collaborator() {
	}

	public Collaborator(final String name) {
		this.name = name;
	}

	public Collaborator(final String name, final String role, final String status, final String username) {
		this.name = name;
		this.role = role;
		this.status = status;
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
	
	public CollaboratorStatus getCollaboratorStatus() {
		return CollaboratorStatus.fromString(status);
	}

	@Override
	public String toString() {
		return name;
	}

}
