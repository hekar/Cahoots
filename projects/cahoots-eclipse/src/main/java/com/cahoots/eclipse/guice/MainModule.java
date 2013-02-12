package com.cahoots.eclipse.guice;

import org.eclipse.jetty.websocket.WebSocketClientFactory;

import com.cahoots.chat.Chat;
import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.http.tools.CahootsHttpClient;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.collab.share.ShareDocumentSessionManager;
import com.cahoots.eclipse.collab.share.ShareDocumentManager;
import com.cahoots.eclipse.op.OpSessionManager;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class MainModule implements Module {

	@Override
	public void configure(final Binder binder) {
		binder.bind(CahootsSocket.class).in(Singleton.class);
		binder.bind(Chat.class).in(Singleton.class);
		binder.bind(CahootsConnection.class).in(Singleton.class);
		binder.bind(ShareDocumentManager.class).in(Singleton.class);
		binder.bind(OpSessionManager.class).in(Singleton.class);		
		binder.bind(ShareDocumentSessionManager.class).in(Singleton.class);		
		binder.bind(WebSocketClientFactory.class);
		binder.bind(CahootsHttpClient.class);
	}

}
