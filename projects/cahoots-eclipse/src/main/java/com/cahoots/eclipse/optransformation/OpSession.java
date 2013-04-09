package com.cahoots.eclipse.optransformation;


public class OpSession {
	private final OpMemento memento;
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

}
