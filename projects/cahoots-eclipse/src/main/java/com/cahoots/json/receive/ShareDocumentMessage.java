package com.cahoots.json.receive;

import com.google.common.base.Objects;

public class ShareDocumentMessage {
	private String service;
	private String type;
	private String sharer;
	private String documentId;
	private String opId;

	public ShareDocumentMessage(final String service, final String type,
			final String sharer, final String documentId, final String opId) {
		this.service = service;
		this.type = type;
		this.sharer = sharer;
		this.documentId = documentId;
		this.opId = opId;
	}

	public String getService() {
		return service;
	}

	public void setService(final String service) {
		this.service = service;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getSharer() {
		return sharer;
	}

	public void setSharer(final String sharer) {
		this.sharer = sharer;
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

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ShareDocumentMessage) {
			final ShareDocumentMessage other = (ShareDocumentMessage) obj;
			return Objects.equal(this.documentId, other.documentId)
					&& Objects.equal(this.opId, other.opId)
					&& Objects.equal(this.service, other.service)
					&& Objects.equal(this.type, other.type)
					&& Objects.equal(this.sharer, other.sharer);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.documentId, this.opId, this.service,
				this.type, this.sharer);
	}

}
