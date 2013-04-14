package com.cahoots.connection.serialize.receive;

import com.cahoots.eclipse.optransformation.OpTransformation;

public class OpInsertMessage extends OpTransformation {

	private final String service = "op";
	private final String type = "insert";

	private String user;

	/**
	 * The contents of the message
	 */
	private String content;

	/**
	 * Document id
	 */
	private String documentId;

	/**
	 * Operational session id
	 */
	private String opId;

	public OpInsertMessage() {
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String contents) {
		this.content = contents;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(final String documentId) {
		this.documentId = documentId;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(final String opId) {
		this.opId = opId;
	}

	public String getService() {
		return service;
	}

	public String getType() {
		return type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

}
