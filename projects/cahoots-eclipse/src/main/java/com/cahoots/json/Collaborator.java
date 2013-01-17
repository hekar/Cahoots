package com.cahoots.json;

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

	public Collaborator(String name, String role, String status, String username) {
		this.name = name;
		this.role = role;
		this.status = status;
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return name;
	}

}
