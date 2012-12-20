package com.cahoots.json.receive;

import com.cahoots.eclipse.op.OpTransformation;

public class OpInsertMessage extends OpTransformation {
	/**
	 * The cursor index that the message was inserted at
	 */
	private int start;

	/**
	 * The contents of the message
	 */
	private String contents;

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

	public int getStart() {
		return start;
	}

	public void setStart(final int start) {
		this.start = start;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(final String contents) {
		this.contents = contents;
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

}
