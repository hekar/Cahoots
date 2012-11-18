package com.cahoots.eclipse;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.eclipse.jetty.websocket.WebSocketClient;
import com.cahoots.websocket.CahootsSocket;

public class Activator extends AbstractUIPlugin {

	@SuppressWarnings("unused")
	private static Activator plugin;
	private static String authToken;
	private static String server;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

        WebSocketClientFactory factory = new WebSocketClientFactory();
        factory.setBufferSize(4096);
        factory.start();
        
        WebSocketClient client = factory.newWebSocketClient();
        client.setMaxIdleTime(30000);
        
        CahootsSocket.getInstance().setClient(client);
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	public static String getAuthToken() {
		return authToken;
	}

	public static void setAuthToken(String authToken) {
		Activator.authToken = authToken;
	}

	public static String getServer() {
		return server;
	}
	
	public static void setServer(String server){
		Activator.server = server;
	}

}
