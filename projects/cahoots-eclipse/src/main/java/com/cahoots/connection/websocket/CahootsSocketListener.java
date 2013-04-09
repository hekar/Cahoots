package com.cahoots.connection.websocket;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;

/**
 * TODO: Create a separation of concerns between the CahootsSocket class
 * (rename it to CahootsService) and this class.
 * 
 * For now, this class handles receiving of websocket requests
 */
public class CahootsSocketListener implements WebSocketTextListener {

	private final CahootsWebSocket cahootsSocket;
	private final AtomicLong received = new AtomicLong(0);
	private final Set<CahootsSocketListener> members = new CopyOnWriteArraySet<CahootsSocketListener>();

	public CahootsSocketListener(final CahootsWebSocket cahootsSocket) {
		this.cahootsSocket = cahootsSocket;
	}

	@Override
	public void onMessage(final String message) {
		received.incrementAndGet();
		cahootsSocket.onMessage(message);
	}

	@Override
	public void onOpen(final WebSocket websocket) {
		members.add(this);
	}

	@Override
	public void onClose(final WebSocket websocket) {
		members.remove(this);
	}

	@Override
	public void onError(final Throwable t) {
	}

	@Override
	public void onFragment(final String arg0, final boolean arg1) {
	}
}