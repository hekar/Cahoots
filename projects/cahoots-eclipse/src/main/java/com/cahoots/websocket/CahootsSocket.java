package com.cahoots.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;

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
import com.cahoots.events.UserChangeEvent;
import com.cahoots.events.UserChangeEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.MessageBase;
import com.cahoots.json.ReceiveAllUsersMessage;
import com.cahoots.json.ReceiveUserStatusMessage;
import com.cahoots.json.receive.OpDeleteMessage;
import com.cahoots.json.receive.OpInsertMessage;
import com.cahoots.json.receive.OpReplaceMessage;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.receive.UnShareDocumentMessage;
import com.google.gson.Gson;

@SuppressWarnings("unchecked")
public class CahootsSocket implements WebSocket.OnTextMessage,
		WebSocket.OnBinaryMessage {

	private final AtomicLong sent = new AtomicLong(0);
	private final AtomicLong received = new AtomicLong(0);
	private final Set<CahootsSocket> members = new CopyOnWriteArraySet<CahootsSocket>();

	private Connection connection;
	private static CahootsSocket instance;
	private WebSocketClient client;

	/**
	 * Due to java's type erasure, it is not possible to make this listener
	 * collection type safe
	 */
	@SuppressWarnings({ "serial", "rawtypes" })
	private Map<Class<? extends GenericEventListener>, List> listeners = new HashMap<Class<? extends GenericEventListener>, List>() {
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
		}
	};

	private List<UserChangeEventListener> loginListeners = new ArrayList<UserChangeEventListener>();
	private List<DisconnectEventListener> disconnectListeners = new ArrayList<DisconnectEventListener>();
	private List<ConnectEventListener> connectListeners = new ArrayList<ConnectEventListener>();

	private CahootsSocket() {
	}

	public static CahootsSocket getInstance() {
		if (instance == null) {
			instance = new CahootsSocket();
		}
		return instance;
	}

	@Override
	public void onClose(int arg0, String arg1) {
		members.remove(this);

	}

	@Override
	public void onOpen(Connection arg0) {
		members.add(this);
	}

	@Override
	public void onMessage(String message) {
		received.incrementAndGet();
		Gson gson = new Gson();
		MessageBase base = gson.fromJson(message, MessageBase.class);
		if ("users".equals(base.service)) {
			if ("all".equals(base.type)) {
				ReceiveAllUsersMessage msg = gson.fromJson(message,
						ReceiveAllUsersMessage.class);
				for (Collaborator c : msg.users) {
					for (UserChangeEventListener listener : loginListeners) {
						listener.userConnected(new UserChangeEvent(c));
					}
				}
			} else if ("status".equals(base.type)) {
				ReceiveUserStatusMessage msg = gson.fromJson(message,
						ReceiveUserStatusMessage.class);
				for (UserChangeEventListener listener : loginListeners) {
					listener.userConnected(new UserChangeEvent(msg.user));
				}
			}
		} else if ("op".equals(base.service)) {
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
		}
	}

	private <K, T> void fireEvents(String eventType,
			Class<K> eventListenerClazz, Class<T> clazz, MessageBase base,
			String message, Gson gson) {

		List<? extends GenericEventListener<T>> listeners = (List<? extends GenericEventListener<T>>) this.listeners
				.get(eventListenerClazz);
		if (eventType.equals(base.type)) {
			T msg = gson.fromJson(message, clazz);
			for (GenericEventListener<T> listener : listeners) {
				listener.onEvent(msg);
			}
		}
	}

	public void disconnect() {
		if (connection != null) {
			connection.close();
			for (DisconnectEventListener listener : disconnectListeners) {
				listener.userDisconnected(new DisconnectEvent());
			}
		}
		connection = null;
	}

	public void connect(String server, String authToken)
			throws InterruptedException, ExecutionException, IOException,
			URISyntaxException {
		disconnect();
		connection = client.open(
				new URI("ws://" + server + "/app/message?auth_token="
						+ authToken), this).get();
		for (ConnectEventListener listener : connectListeners) {
			listener.connected(new ConnectEvent());
		}
	}

	public String send(Object obj) {
		try {
			String json = new Gson().toJson(obj);
			send(json);
			return json;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void send(String message) throws IOException {
		if (connection != null) {
			connection.sendMessage(message);
			sent.incrementAndGet();
		}
	}

	public void setClient(WebSocketClient client) {
		this.client = client;
	}

	/**
	 * RESPONSES ARE NOT TYPE SAFE!! TODO: Add Guice and move this to a mock
	 * 
	 * @return
	 */
	public <T extends GenericEventListener<? extends Object>, K> K sendAndWaitForResponse(
			final Object sendMessage, final Class<K> messageClazz,
			final Class<T> clazz) {
		try {
			final Object lock = new Object();
			synchronized (lock) {
				final List<K> events = new ArrayList<K>();
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						listeners.get(clazz).add(new GenericEventListener<K>() {
							@Override
							public void onEvent(K msg) {
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
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onMessage(byte[] arg0, int arg1, int arg2) {
		System.out.println(new String(arg0));
	}

	public void addUserLoginEventListener(UserChangeEventListener listener) {
		loginListeners.add(listener);
	}

	public void addDisconnectEventListener(DisconnectEventListener listener) {
		disconnectListeners.add(listener);
	}

	public void addConnectEventListener(ConnectEventListener listener) {
		connectListeners.add(listener);
	}

	public void addOpInsertEventListener(OpInsertEventListener listener) {
		listeners.get(OpInsertEventListener.class).add(listener);
	}

	public void addShareDocumentEventListener(
			ShareDocumentEventListener listener) {
		listeners.get(ShareDocumentEventListener.class).add(listener);
	}

}
