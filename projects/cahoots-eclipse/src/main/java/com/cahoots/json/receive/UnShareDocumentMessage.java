package com.cahoots.json.receive;

public class UnShareDocumentMessage {
	private String service;
	private String type;
	private String sharer;
	private String opId;
	private String documentId;

	public UnShareDocumentMessage(String service, String type, String sharer,
			String opId, String documentId) {
		this.service = service;
		this.type = type;
		this.sharer = sharer;
		this.opId = opId;
		this.documentId = documentId;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSharer() {
		return sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

}
