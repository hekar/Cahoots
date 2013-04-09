package com.cahoots.connection;

public class ConnectionDetails {

	private String username;
	private String password;
	private String authToken;
	private String server;

	public ConnectionDetails() {
		disconnect();
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

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setAuthToken(final String authToken) {
		this.authToken = authToken;
	}

	public void setServer(final String server) {
		this.server = server;
	}

	public boolean isLoggedInUser(final String username) {
		final String loggedInUser = getUsername();
		if (loggedInUser == null) {
			return false;
		}

		return loggedInUser.equals(username);
	}

	@Deprecated
	public boolean isAuthenticated() {
		return getAuthToken() != null && !getAuthToken().isEmpty();
	}

	public void disconnect() {
		setUsername("");
		setPassword("");
		setAuthToken("");
		setServer("");
	}

}
