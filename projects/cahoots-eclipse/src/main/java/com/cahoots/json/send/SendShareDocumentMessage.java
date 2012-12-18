package com.cahoots.json.send;

import java.util.ArrayList;
import java.util.List;

public class SendShareDocumentMessage {
	private final String service = "op";
	private final String type = "share";
	
	private String user;
	private String documentId;
	private List<String> collaborators;

	public SendShareDocumentMessage(String user, String documentId,
			List<String> collaborators) {
		super();
		this.user = user;
		this.documentId = documentId;
		this.collaborators = collaborators;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public List<String> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(List<String> collaborators) {
		this.collaborators = collaborators;
	}

	public String getService() {
		return service;
	}

	public String getType() {
		return type;
	}

	
}
