package com.cahoots.eclipse.op;

import java.util.Arrays;

public class OpDocument {
	private final String opId;
	private final String documentId;

	public OpDocument(final String opId, final String documentId) {
		this.opId = opId;
		this.documentId = documentId;
	}

	public String getOpId() {
		return opId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public String getFilename() {
		// TODO: Add error handling
		final String[] split = documentId.split("/");
		return split[split.length - 1];
	}

	
}
