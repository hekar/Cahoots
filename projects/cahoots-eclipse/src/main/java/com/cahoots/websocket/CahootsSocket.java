package com.cahoots.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
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
import com.cahoots.events.UserChangeEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.MessageBase;
import com.cahoots.json.receive.OpDeleteMessage;
import com.cahoots.json.receive.OpInsertMessage;
import com.cahoots.json.receive.OpReplaceMessage;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.receive.UnShareDocumentMessage;
import com.cahoots.json.receive.UserChangeMessage;
import com.cahoots.json.receive.UserListMessage;
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
	    put(UserChangeEventListener.class,
		    new ArrayList<UserChangeEventListener>());
	}
    };
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
    public void onClose(final int arg0, final String arg1) {
	members.remove(this);

    }

    @Override
    public void onOpen(final Connection arg0) {
	members.add(this);
    }

    @Override
    public void onMessage(final String message) {
	received.incrementAndGet();
	final Gson gson = new Gson();
	final MessageBase base = gson.fromJson(message, MessageBase.class);
	if ("users".equals(base.service)) {
	    if ("all".equals(base.type)) {
		UserListMessage msg = gson.fromJson(message,
			UserListMessage.class);
		for (Collaborator user : msg.users) {
		    fireEvents("all", UserChangeEventListener.class,
			    UserChangeMessage.class, base,
			    gson.toJson(new UserChangeMessage(user)), gson);
		}
	    } else {
		fireEvents("status", UserChangeEventListener.class,
			UserChangeMessage.class, base, message, gson);
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

    private <K, T> void fireEvents(final String eventType,
	    final Class<K> eventListenerClazz, final Class<T> clazz,
	    final MessageBase base, final String message, final Gson gson) {

	if (eventType.equals(base.type)) {
	    final List<? extends GenericEventListener<T>> listeners = this.listeners
		    .get(eventListenerClazz);
	    final T msg = gson.fromJson(message, clazz);
	    for (final GenericEventListener<T> listener : listeners) {
		listener.onEvent(msg);
	    }
	}
    }

    public void disconnect() {
	if (connection != null) {
	    connection.close();
	    for (final DisconnectEventListener listener : disconnectListeners) {
		listener.userDisconnected(new DisconnectEvent());
	    }
	}
	connection = null;
    }

    public void connect(final String server, final String authToken) {
	try {
	    disconnect();
	    connection = client.open(
		    new URI("ws://" + server + "/app/message?auth_token="
			    + authToken), this).get();
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
	    connection.sendMessage(message);
	    sent.incrementAndGet();
	}
    }

    public void setClient(final WebSocketClient client) {
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
		final Thread thread = new Thread(new Runnable() {
		    @Override
		    public void run() {
			listeners.get(clazz).add(new GenericEventListener<K>() {
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

    @Override
    public void onMessage(final byte[] arg0, final int arg1, final int arg2) {
	System.out.println(new String(arg0));
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

}
