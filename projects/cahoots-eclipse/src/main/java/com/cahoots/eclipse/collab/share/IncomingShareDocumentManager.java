package com.cahoots.eclipse.collab.share;

import java.util.ArrayList;
import java.util.List;

import com.cahoots.json.receive.ShareDocumentMessage;

public class IncomingShareDocumentManager {

	private final List<ShareDocumentMessage> sharedDocuments = new ArrayList<ShareDocumentMessage>();

	public IncomingShareDocumentManager() {
	}

	public void register(final ShareDocumentMessage message) {
		sharedDocuments.add(message);
	}

	public void unregister(final ShareDocumentMessage message) {
		sharedDocuments.remove(message);
	}
}
