package com.cahoots.eclipse.collab.share;

import javax.inject.Inject;

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.event.EventRegistrar;
import com.cahoots.eclipse.indigo.log.Log;

/**
 * Clean up this piece of fecal matter with giant set of explosives
 * 
 * @author Hekar
 * 
 */
public class ShareDocumentRegistrar implements EventRegistrar {

	static Log logger = Log.getLogger(ShareDocumentRegistrar.class);

	private final CahootsSocket cahootsSocket;
	private final IncomingDocumentShare incomingDocumentShare;

	@Inject
	public ShareDocumentRegistrar(final CahootsSocket cahootsSocket,
			final IncomingDocumentShare incomingDocumentShare) {
		this.cahootsSocket = cahootsSocket;
		this.incomingDocumentShare = incomingDocumentShare;
	}

	@Override
	public void registerEvents() {
		setupDefaultNotifications();
	}

	private void setupDefaultNotifications() {
		cahootsSocket.addShareDocumentEventListener(incomingDocumentShare);
	}

}
