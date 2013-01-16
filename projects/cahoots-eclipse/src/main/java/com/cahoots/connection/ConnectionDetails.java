package com.cahoots.connection;

public class ConnectionDetails {

	private final String username;
	private final String password;
	private final String authToken;
	private final String server;

	public ConnectionDetails(String username, String password,
			String authToken, String server) {
		this.username = username;
		this.password = password;
		this.authToken = authToken;
		this.server = server;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getServer() {
		return server;
	}

}
