package com.cahoots.eclipse.collab.share;

import java.util.ArrayList;
import java.util.List;

import com.cahoots.json.receive.ShareDocumentMessage;

public class ShareDocumentSessionManager {

	private final List<ShareDocumentSessionRegisterListener> registerListeners = new ArrayList<ShareDocumentSessionRegisterListener>();
	private final List<ShareDocumentMessage> sharedDocuments = new ArrayList<ShareDocumentMessage>();

	public ShareDocumentSessionManager() {
	}

	public void register(final ShareDocumentMessage message) {
		sharedDocuments.add(message);
	}

	public void unregister(final ShareDocumentMessage message) {
		sharedDocuments.remove(message);
	}

	public void addOnRegisterListener(
			final ShareDocumentSessionRegisterListener listener) {
		registerListeners.add(listener);
	}
}
