package com.cahoots.json;

public class Collaborator {

	public String name;

	public String role;

	public String status;

	public Collaborator() {
	}

	public Collaborator(final String name) {
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
