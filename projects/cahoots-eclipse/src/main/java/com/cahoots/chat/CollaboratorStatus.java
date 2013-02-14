package com.cahoots.chat;

public enum CollaboratorStatus {
	// I give up; following the standard Java uppercase constant naming scheme
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
