package com.cahoots.connection;


/**
 * Not sure about what to do with this class.
 * 
 * Basically, should we unify the HTTP and WebSocket clients or keep them
 * separate?
 * 
 * This is the question that ultimately determines the purpose and name of this
 * class
 */
public class CahootsConnection {

	private ConnectionDetails details;

	public CahootsConnection() {
		details = new ConnectionDetails("", "", "", "");
	}

	public String getUsername() {
		return details.getUsername();
	}

	public String getPassword() {
		return details.getPassword();
	}

	public String getAuthToken() {
		return details.getAuthToken();
	}

	public String getServer() {
		return details.getServer();
	}

	public void updateConnectionDetails(ConnectionDetails details) {
		this.details = details;
	}

	@Deprecated
	public boolean isAuthenticated() {
		return getAuthToken() != null && !getAuthToken().isEmpty();
	}

}
