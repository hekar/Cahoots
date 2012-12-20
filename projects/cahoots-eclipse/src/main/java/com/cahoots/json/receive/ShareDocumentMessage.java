package com.cahoots.json.receive;

public class ShareDocumentMessage {
	private String service;
	private String type;
	private String sharer;
	private String documentId;
	private String opId;

	public ShareDocumentMessage(String service, String type, String sharer,
			String documentId, String opId) {
		this.service = service;
		this.type = type;
		this.sharer = sharer;
		this.documentId = documentId;
		this.opId = opId;
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
