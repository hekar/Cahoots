package com.cahoot.client.connection;

public class ConnectionException extends RuntimeException {

	/**
	 * 
	 */
	public ConnectionException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ConnectionException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConnectionException(Throwable cause) {
		super(cause);
	}

}
