package com.cahoots.json.send;

import java.util.List;

public class SendUnShareDocumentMessage {
	private String user;
	private String opId;
	private List<String> collaborators;

	public SendUnShareDocumentMessage(String user, String opId,
			List<String> collaborators) {
		this.user = user;
		this.opId = opId;
		this.collaborators = collaborators;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public List<String> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(List<String> collaborators) {
		this.collaborators = collaborators;
	}

}
