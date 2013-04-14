package com.cahoots.connection.serialize.receive;

import com.cahoots.eclipse.optransformation.OpTransformation;

public class OpDeleteMessage extends OpTransformation {
	private final String service = "op";
	private final String type = "delete";

	private String opId;
	private String documentId;

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
		return getReplacementLength() - getStart();
	}

	public String toString() {
		return String.format(
				"Del:\tuser:%s, ts:%d, start:%d, end:%d, moved:%d, ind:%d, lc:%d, rc:%d",
				getUser().substring(getUser().length() - 1), getTickStamp(), getStart(), 
				getReplacementLength(), getMoved(), getIndex(), getLocalCount(), getRemoteCount());
	}
	
}
