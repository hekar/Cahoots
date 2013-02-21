package com.cahoots.eclipse.op;

import java.util.ArrayList;
import java.util.List;

public class OpSession {
	private final OpMemento memento;
	private final List<String> collaborators = new ArrayList<String>();
	private final OpSynchronizedClock clock;

	public OpSession(final OpMemento memento, final OpSynchronizedClock clock) {
		this.memento = memento;
		this.clock = clock;
	}

	public OpMemento getMemento() {
		return memento;
	}

	public OpSynchronizedClock getClock() {
		return clock;
	}

	public List<String> getCollaborators() {
		return collaborators;
	}

}
