package com.cahoots.connection.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.http.tools.CahootsHttpClient;
import com.cahoots.connection.http.tools.CahootsHttpResponseReceivedListener;
import com.cahoots.eclipse.indigo.log.Log;
import com.cahoots.eclipse.indigo.widget.TextEditorTools;
import com.cahoots.events.ChatReceivedEventListener;
import com.cahoots.events.ConnectEvent;
import com.cahoots.events.ConnectEventListener;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;
import com.cahoots.events.GenericEventListener;
import com.cahoots.events.OpDeleteEventListener;
import com.cahoots.events.OpInsertEventListener;
import com.cahoots.events.OpReplaceEventListener;
import com.cahoots.events.ShareDocumentEventListener;
import com.cahoots.events.UnShareDocumentEventListener;
import com.cahoots.events.UserChangeEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.MessageBase;
import com.cahoots.json.receive.ChatReceiveMessage;
import com.cahoots.json.receive.OpDeleteMessage;
import com.cahoots.json.receive.OpInsertMessage;
import com.cahoots.json.receive.OpReplaceMessage;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.receive.UnShareDocumentMessage;
import com.cahoots.json.receive.UserChangeMessage;
import com.cahoots.json.receive.UserListMessage;
import com.google.gson.Gson;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

@SuppressWarnings("unchecked")
public class CahootsSocket {

	private static final Log logger = Log.getLogger(CahootsSocket.class);

	/**
	 * Timeout is 30 minutes long.
	 * 
	 * Format is in milliseconds
	 */
	private static final int TIMEOUT = 1800000;

	private final AtomicLong sent = new AtomicLong(0);

	private final CahootsConnection cahootsConnection;
	private final TextEditorTools editorTools;

	/**
	 * Due to java's type erasure, it is not possible to make this listener
	 * collection type safe
	 */
	@SuppressWarnings({ "serial", "rawtypes" })
	private final Map<Class<? extends GenericEventListener>, List> listeners = new HashMap<Class<? extends GenericEventListener>, List>() {
		{
			put(ShareDocumentEventListener.class,
					new ArrayList<ShareDocumentEventListener>());
			put(UnShareDocumentEventListener.class,
					new ArrayList<UnShareDocumentEventListener>());
			put(OpInsertEventListener.class,
					new ArrayList<OpInsertEventListener>());
			put(OpReplaceEventListener.class,
					new ArrayList<OpReplaceEventListener>());
			put(OpDeleteEventListener.class,
					new ArrayList<OpDeleteEventListener>());
			put(UserChangeEventListener.class,
					new ArrayList<UserChangeEventListener>());
			put(ChatReceivedEventListener.class,
					new ArrayList<ChatReceivedEventListener>());
		}
	};
	private List<DisconnectEventListener> disconnectListeners = new ArrayList<DisconnectEventListener>();
	private List<ConnectEventListener> connectListeners = new ArrayList<ConnectEventListener>();

	private ListenableFuture<WebSocket> connection;

	@Inject
	public CahootsSocket(final CahootsConnection cahootsConnection,
			final WebSocketClientFactory factory,
			final TextEditorTools editorTools) {

		this.cahootsConnection = cahootsConnection;
		this.editorTools = editorTools;

		try {
			factory.setBufferSize(4096);
			factory.start();

			// client = factory.newWebSocketClient();
			// client.setMaxIdleTime(TIMEOUT);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO: Replace with real check
	 */
	@SuppressWarnings("deprecation")
	public boolean isConnected() {
		return cahootsConnection.isAuthenticated();
	}

	private <K, T> void fireEvents(final String eventType,
			final Class<K> eventListenerClazz, final Class<T> clazz,
			final MessageBase base, final String message, final Gson gson) {

		if (eventType.equals(base.getType())) {
			final List<? extends GenericEventListener<T>> listeners = this.listeners
					.get(eventListenerClazz);
			final T msg = gson.fromJson(message, clazz);
			for (final GenericEventListener<T> listener : listeners) {
				listener.onEvent(msg);
			}
		}
	}

	public void connect(final String username, final String password,
			final String server) {
		final List<NameValuePair> data = new LinkedList<NameValuePair>();
		data.add(new NameValuePair("username", username));
		data.add(new NameValuePair("password", password));

		final CahootsHttpClient client = new CahootsHttpClient();
		client.post(server, "/app/login", data,
				new CahootsHttpResponseReceivedListener() {
					@Override
					public void onReceive(final int statusCode,
							final HttpMethodBase method) {
						try {
							if (statusCode == 200) {
								final String authToken = method
										.getResponseBodyAsString();

								cahootsConnection
										.updateConnectionDetails(new ConnectionDetails(
												username, password, authToken,
												server));

								connect(server, authToken);
							} else {
								throw new RuntimeException(
										"Error connecting to server: "
												+ method.getResponseBodyAsString());
							}
						} catch (final IOException e) {
							throw new RuntimeException(
									"Error connecting to server", e);
						}
					}
				});

		addDefaultNotifications(this);
	}

	/**
	 * TODO: Move to another class
	 * 
	 * @param cahootsSocket
	 */
	private void addDefaultNotifications(CahootsSocket cahootsSocket) {
		cahootsSocket.addOpInsertEventListener(new OpInsertEventListener() {
			@Override
			public void onEvent(final OpInsertMessage msg) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						try {
							int start = msg.getStart();
							String contents = msg.getContents();
							Long tickStamp = msg.getTickStamp();
							
							ITextEditor textEditor = editorTools.getTextEditor();
							IDocumentProvider documentProvider = textEditor.getDocumentProvider();
							IDocument document = (IDocument) documentProvider.getDocument(textEditor.getEditorInput());
							document.replace(start, 0, contents);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	public void disconnect() {
		if (connection != null) {
			connection.cancel(true);

			for (final DisconnectEventListener listener : disconnectListeners) {
				listener.userDisconnected(new DisconnectEvent());
			}
		}
		connection = null;
	}

	public void connect(final String server, final String authToken) {
		try {
			disconnect();
			AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder().build();
			AsyncHttpClient c = new AsyncHttpClient(cf);
		
			String uriString = String.format("ws://%s/app/message?auth_token=%s", server, authToken);
			CahootsSocketClient cahootsSocketClient = new CahootsSocketClient();
			connection = c.prepareGet(uriString).execute(new WebSocketUpgradeHandler.Builder()
				.addWebSocketListener(cahootsSocketClient).build());

			for (final ConnectEventListener listener : connectListeners) {
				listener.connected(new ConnectEvent());
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String send(final Object obj) {
		try {
			final String json = new Gson().toJson(obj);
			send(json);
			return json;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void send(final String message) throws IOException {
		if (connection != null) {
			try {
				connection.get().sendTextMessage(message);
				sent.incrementAndGet();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * WARNING: For Testing purposes only
	 * 
	 * RESPONSES ARE NOT TYPE SAFE!! TODO: Add Guice and move this to a mock
	 * 
	 * @return
	 */
	@Deprecated
	public <T extends GenericEventListener<? extends Object>, K> K sendAndWaitForResponse(
			final Object sendMessage, final Class<K> messageClazz,
			final Class<T> eventClazz) {
		try {
			final Object lock = new Object();
			synchronized (lock) {
				final List<K> events = new ArrayList<K>();
				final Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						listeners.get(eventClazz).add(
								new GenericEventListener<K>() {
									@Override
									public void onEvent(final K msg) {
										synchronized (lock) {
											events.add(msg);
											lock.notifyAll();
										}
									}
								});
					}
				});
				thread.start();
				Thread.sleep(32);
				send(sendMessage);
				lock.wait();

				if (events.size() > 0) {
					return events.get(0);
				} else {
					throw new RuntimeException("No responses received");
				}
			}
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void addUserLoginEventListener(final UserChangeEventListener listener) {
		listeners.get(UserChangeEventListener.class).add(listener);
	}

	public void addDisconnectEventListener(
			final DisconnectEventListener listener) {
		disconnectListeners.add(listener);
	}

	public void addConnectEventListener(final ConnectEventListener listener) {
		connectListeners.add(listener);
	}

	public void addOpInsertEventListener(final OpInsertEventListener listener) {
		listeners.get(OpInsertEventListener.class).add(listener);
	}

	public void addShareDocumentEventListener(
			final ShareDocumentEventListener listener) {
		listeners.get(ShareDocumentEventListener.class).add(listener);
	}

	public void addChatReceivedEventListener(
			final ChatReceivedEventListener listener) {
		listeners.get(ChatReceivedEventListener.class).add(listener);
	}

	/**
	 * TODO: Create a separation of concerns between the CahootsSocket class
	 * (rename it to CahootsService) and this class.
	 * 
	 * For now, this class handles receiving of websocket requests
	 */
	private class CahootsSocketClient implements WebSocketTextListener {

		private final AtomicLong received = new AtomicLong(0);
		private final Set<CahootsSocketClient> members = new CopyOnWriteArraySet<CahootsSocketClient>();

		public CahootsSocketClient() {
		}

		@Override
		public void onMessage(String message) {
			received.incrementAndGet();
			final Gson gson = new Gson();
			final MessageBase base = gson.fromJson(message, MessageBase.class);

			logger.debug("Message received");
			logger.debug(message);

			if ("users".equals(base.getService())) {
				if ("all".equals(base.getType())) {
					UserListMessage msg = gson.fromJson(message,
							UserListMessage.class);
					for (Collaborator user : msg.getUsers()) {
						fireEvents("all", UserChangeEventListener.class,
								UserChangeMessage.class, base,
								gson.toJson(new UserChangeMessage(user)), gson);
					}
				} else {
					fireEvents("status", UserChangeEventListener.class,
							UserChangeMessage.class, base, message, gson);
				}
			} else if ("op".equals(base.getService())) {
				fireEvents("shared", ShareDocumentEventListener.class,
						ShareDocumentMessage.class, base, message, gson);
				fireEvents("unshared", UnShareDocumentEventListener.class,
						UnShareDocumentMessage.class, base, message, gson);
				fireEvents("insert", OpInsertEventListener.class,
						OpInsertMessage.class, base, message, gson);
				fireEvents("replace", OpReplaceEventListener.class,
						OpReplaceMessage.class, base, message, gson);
				fireEvents("delete", OpDeleteEventListener.class,
						OpDeleteMessage.class, base, message, gson);
			} else if ("chat".equals(base.getService())) {
				fireEvents("receive", ChatReceivedEventListener.class,
						ChatReceiveMessage.class, base, message, gson);
			}

		}

		@Override
		public void onOpen(WebSocket websocket) {
			members.add(this);
		}

		@Override
		public void onClose(WebSocket websocket) {
			members.remove(this);
		}

		@Override
		public void onError(Throwable t) {
		}

		@Override
		public void onFragment(String arg0, boolean arg1) {
		}
	}
}
