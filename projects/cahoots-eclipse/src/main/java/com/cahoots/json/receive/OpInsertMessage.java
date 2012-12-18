package com.cahoots.json.receive;

public class OpInsertMessage {
	/**
	 * The cursor index that the message was inserted at
	 */
	private int start;

	/**
	 * The contents of the message
	 */
	private String contents;

	/**
	 * The tickstamp that the message was created with
	 */
	private long tickStamp;

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

	public void setStart(int start) {
		this.start = start;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public long getTickStamp() {
		return tickStamp;
	}

	public void setTickStamp(long tickStamp) {
		this.tickStamp = tickStamp;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

}
