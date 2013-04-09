package com.cahoots.connection.serialize;

import java.util.ArrayList;
import java.util.List;

public class Collaboration {
	private String opId;
	private String documentId;
	private List<String> collaborators;

	public Collaboration(final String opId, final String documentId,
			final List<String> collaborators) {
		this.opId = opId;
		this.documentId = documentId;
		this.collaborators = new ArrayList<String>(collaborators);
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(final String opId) {
		this.opId = opId;
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
}
