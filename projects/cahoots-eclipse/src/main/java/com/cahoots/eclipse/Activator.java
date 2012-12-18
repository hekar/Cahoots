package com.cahoots.eclipse;

import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.cahoots.websocket.CahootsSocket;

public class Activator extends AbstractUIPlugin {

	@SuppressWarnings("unused")
	private static Activator plugin;
	private static String authToken;
	private static String server;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

        try {
			initializeCahootsSocket();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 */
	public static void initializeCahootsSocket() throws Exception {
		final WebSocketClientFactory factory = new WebSocketClientFactory();
        factory.setBufferSize(4096);
        factory.start();
        
        final WebSocketClient client = factory.newWebSocketClient();
        client.setMaxIdleTime(30000);
        
        CahootsSocket.getInstance().setClient(client);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	public static void connect(final String username,
			final String password,
			final String server) {	
		final HttpClient client = new HttpClient();
		final PostMethod method = new PostMethod( "http://" + server + "/app/login");
		final List<NameValuePair> data = new LinkedList<NameValuePair>();
		data.add( new NameValuePair("username", username));
		data.add( new NameValuePair("password", password));
		
		method.setRequestBody(data.toArray(new NameValuePair[data.size()]));
		try
		{
			final int statusCode = client.executeMethod(method);
			if(statusCode == 200)
			{
				final String authToken = new String(method.getResponseBody());
				setAuthToken(authToken);
				setServer(server);
	
				CahootsSocket.getInstance().connect(server, authToken);
			}
			else
			{
				throw new SocketException("Error connecting to server");
			}
		}
		catch(final Exception ex)
		{
			throw new RuntimeException("Error connecting to server", ex);
		}
	}

	public static String getAuthToken() {
		return authToken;
	}

	public static void setAuthToken(final String authToken) {
		Activator.authToken = authToken;
	}

	public static String getServer() {
		return server;
	}
	
	public static void setServer(final String server){
		Activator.server = server;
	}

}
