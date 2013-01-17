package com.cahoots.eclipse.guice;

import org.eclipse.jetty.websocket.WebSocketClientFactory;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.http.tools.CahootsHttpClient;
import com.cahoots.connection.websocket.CahootsSocket;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class MainModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(CahootsSocket.class).in(Singleton.class);
		binder.bind(CahootsConnection.class).in(Singleton.class);
		
		binder.bind(WebSocketClientFactory.class);
		binder.bind(CahootsHttpClient.class);
	}

}
