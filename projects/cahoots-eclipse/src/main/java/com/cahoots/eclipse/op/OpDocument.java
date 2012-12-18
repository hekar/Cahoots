package com.cahoots.eclipse.op;

public class OpDocument {
	private final String startContents;
	private final String opId;
	private final String documentId;

	public OpDocument(final String startContents, final String opId, final String documentId) {
		this.startContents = startContents;
		this.opId = opId;
		this.documentId = documentId;
	}

	public String getStartContents() {
		return startContents;
	}

	public String getOpId() {
		return opId;
	}

	public String getDocumentId() {
		return documentId;
	}

}
