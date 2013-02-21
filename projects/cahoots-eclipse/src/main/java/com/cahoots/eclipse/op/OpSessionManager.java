package com.cahoots.eclipse.op;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OpSessionManager {
	private final Map<String, OpSession> sessions = new HashMap<String, OpSession>();

	public OpSessionManager() {
	}

	public void addSession(final String opId, final OpSession session) {
		sessions.put(opId, session);
	}

	public Set<String> getSessionKeys() {
		return sessions.keySet();
	}

	public void removeSession(final String opId) {
		sessions.remove(opId);
	}
}
