package com.cahoots.serialize.json.send;

import java.util.List;

public class SendShareDocumentMessage {
	private final String service = "op";
	private final String type = "share";

	private String user;
	private String documentId;
	private String fileContents;
	private List<String> collaborators;

	public SendShareDocumentMessage(final String user, final String documentId,
			final List<String> collaborators, final String fileContents) {
		super();
		this.user = user;
		this.documentId = documentId;
		this.collaborators = collaborators;
		this.fileContents = fileContents;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(final String documentId) {
		this.documentId = documentId;
	}

	public List<String> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(final List<String> collaborators) {
		this.collaborators = collaborators;
	}

	public String getService() {
		return service;
	}

	public String getType() {
		return type;
	}

	public String getFileContentes() {
		return fileContents;
	}

}
