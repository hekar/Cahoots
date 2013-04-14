package com.cahoots.eclipse.optransformation;

import org.eclipse.ecf.internal.provisional.docshare.cola.ColaSynchronizer;

public class OpSession {
	private final OpMergeManager memento;
	private final OpSynchronizedClock clock;

	private final boolean initiated;

	/**
	 * 
	 * @param initiated
	 *            - Is the local user the one that initiated the share
	 * @param memento
	 * @param clock
	 */
	public OpSession(final boolean initiated, final OpMergeManager memento, final OpSynchronizedClock clock) {
		this.initiated = initiated;
		this.memento = memento;
		this.clock = clock;
	}

	public OpMergeManager getMemento() {
		return memento;
	}

	public OpSynchronizedClock getClock() {
		return clock;
	}

}
