package com.cahoots.serialize.json.receive;

import java.util.List;

import com.cahoots.serialize.json.MessageBase;

public class CollaboratorsListMessage extends MessageBase {

	private List<String> collaborators;
	private String opId;

	public CollaboratorsListMessage() {
		super("op", "collaborations");
	}

	public CollaboratorsListMessage(final List<String> collaborators,
			final String opId) {
		this();
		this.collaborators = collaborators;
		this.opId = opId;
	}

	public List<String> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(final List<String> collaborators) {
		this.collaborators = collaborators;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(final String opId) {
		this.opId = opId;
	}
}
