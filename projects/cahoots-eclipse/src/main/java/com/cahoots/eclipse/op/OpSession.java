package com.cahoots.eclipse.op;

import java.util.ArrayList;
import java.util.List;

public class OpSession {
	private final OpMemento memento;
	private final List<String> collaborators = new ArrayList<String>();

	public OpSession(final OpMemento memento) {
		this.memento = memento;
	}

	public OpMemento getMemento() {
		return memento;
	}

	public List<String> getCollaborators() {
		return collaborators;
	}

}
