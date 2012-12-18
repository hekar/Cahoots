package com.cahoots.eclipse;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.cahoots.http.tools.CahootsHttpClient;
import com.cahoots.http.tools.CahootsHttpResponseReceivedListener;
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
		final List<NameValuePair> data = new LinkedList<NameValuePair>();
		data.add(new NameValuePair("username", username));
		data.add(new NameValuePair("password", password));

		final CahootsSocket cahootsSocket = CahootsSocket.getInstance();
		final CahootsHttpClient client = new CahootsHttpClient();
		client.post(server, "/app/login", data, new CahootsHttpResponseReceivedListener() {
			@Override
			public void onReceive(final int statusCode, final HttpMethodBase method) {
				try {
					if (statusCode == 200)
					{
						final String authToken = method.getResponseBodyAsString();
						Activator.setAuthToken(authToken);
						Activator.setServer(server);

						cahootsSocket.connect(server, authToken);
					}
					else
					{
						throw new RuntimeException("Error connecting to server: "
								+ method.getResponseBodyAsString());
					}
				} catch (final IOException e) {
					throw new RuntimeException("Error connecting to server", e);
				}
			}
		});
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

	public static void setServer(final String server) {
		Activator.server = server;
	}

}
