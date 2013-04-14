package com.cahoots.chat;

/**
 * Status of an collaborator 
 */
public enum CollaboratorStatus {
	ONLINE, OFFLINE;

	public static CollaboratorStatus fromString(final String status) {
		if ("online".equals(status)) {
			return CollaboratorStatus.ONLINE;
		} else if ("offline".equals(status)) {
			return CollaboratorStatus.OFFLINE;
		} else {
			throw new IllegalArgumentException(String.format(
					"Status: %s not recognized", status));
		}
	}
}
