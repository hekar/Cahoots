package com.cahoots.connection.serialize.receive;

import com.cahoots.eclipse.op.OpTransformation;

public class OpDeleteMessage extends OpTransformation {
	private final String service = "op";
	private final String type = "delete";

	private String user;
	private String opId;
	private String documentId;
	private Integer start;
	private Integer end;
	private String oldContent;

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

	public Integer getStart() {
		return start;
	}

	public void setStart(final Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(final Integer end) {
		this.end = end;
	}

	public String getOldContent() {
		return oldContent;
	}

	public void setOldContent(final String oldContent) {
		this.oldContent = oldContent;
	}

}
