package com.cahoots.events;

import com.cahoots.json.receive.OpInsertMessage;

public class OpInsertEvent {

	private final OpInsertMessage msg;

	public OpInsertEvent(OpInsertMessage msg) {
		this.msg = msg;
	}

	public OpInsertMessage getMessage() {
		return msg;
	}

}
