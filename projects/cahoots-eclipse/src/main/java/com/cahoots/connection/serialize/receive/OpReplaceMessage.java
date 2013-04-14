package com.cahoots.connection.serialize.receive;

import com.cahoots.eclipse.optransformation.OpTransformation;

/**
 * @author Hekar
 *
 */
public class OpReplaceMessage extends OpTransformation {
	private final String service = "op";
	private final String type = "replace";

	private String opId;
	private String documentId;

	public OpReplaceMessage() {
	}

	public String getService() {
		return service;
	}

	public String getType() {
		return type;
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

	@Override
	public int getLength() {
		if (getReplacementLength() == Integer.MAX_VALUE) {
			return getContent().length();
		} else {
			return getContent().length() - getReplacementLength();
		}
	}

	public String toString() {
		return String.format(
				"Rep:\tuser:%s, t:'%s', ts:%d, start: %d, end:%d, moved:%d, ind:%d, tc:%d",
				getUser().substring(getUser().length() - 1), getContent(), getTickStamp(), 
				getStart(), getReplacementLength(), getMoved(), getIndex());
	}

}
