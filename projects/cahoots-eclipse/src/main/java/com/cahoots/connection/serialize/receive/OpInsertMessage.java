package com.cahoots.connection.serialize.receive;

import com.cahoots.eclipse.optransformation.OpTransformation;

public class OpInsertMessage extends OpTransformation {

	private final String service = "op";
	private final String type = "insert";

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

	@Override
	public Integer getReplacementLength() {
		return 0;
	}

	public int getLength() {
		return getContent().length();
	}

	public String toString() {
		return String.format("Ins:\tuser:%s, t:'%s', ts:%d, start:%d, moved:%d, ind:%d, lc:%d, rc:%d", getUser()
				.substring(getUser().length() - 1), getContent(), getTickStamp(), getStart(), getMoved(), getIndex(),
				getLocalCount(), getRemoteCount());
	}

}
