package com.cahoots.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;

import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;
import com.cahoots.events.UserChangeEvent;
import com.cahoots.events.UserChangeEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.MessageBase;
import com.cahoots.json.ReceiveAllUsersMessage;
import com.cahoots.json.ReceiveUserStatusMessage;
import com.google.gson.Gson;

public class CahootsSocket implements WebSocket.OnTextMessage, WebSocket.OnBinaryMessage {
	
    private final AtomicLong sent = new AtomicLong(0);
    private final AtomicLong received = new AtomicLong(0);
    private final Set<CahootsSocket> members = new CopyOnWriteArraySet<CahootsSocket>();
    private Connection connection = null;
    private static CahootsSocket instance = null;
    private WebSocketClient client;
    
    private List<UserChangeEventListener> loginListeners = new ArrayList<UserChangeEventListener>();
    private List<DisconnectEventListener> disconnectListeners = new ArrayList<DisconnectEventListener>();
    

    private CahootsSocket(){}
    
    public static CahootsSocket getInstance()
    {
    	if(instance == null)
    	{
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
        if("users".equals(base.service))
        {
        	if("all".equals(base.type))
        	{
        		ReceiveAllUsersMessage msg = gson.fromJson(message, ReceiveAllUsersMessage.class);
        		for(Collaborator c : msg.users)
        		{
    				for(UserChangeEventListener listener: loginListeners)
    				{
    					listener.UserLoginEvent(new UserChangeEvent(c));
    				}
        		}
        	}
        	else if ("status".equals(base.type))
        	{
        		ReceiveUserStatusMessage msg = gson.fromJson(message, ReceiveUserStatusMessage.class);
        		Collaborator c = new Collaborator();
        		c.status = msg.status;
        		c.name = msg.user;
        		for(UserChangeEventListener listener: loginListeners)
				{
					listener.UserLoginEvent(new UserChangeEvent(c));
				}
        	}
        }
	}
	
	public void disconnect()
    {
		if(connection != null)
		{
	        connection.close();
	        for(DisconnectEventListener listener : disconnectListeners)
	        {
	        	listener.disconnected(new DisconnectEvent());
	        }
		}
		connection = null;
    }
	
	public void connect(String authToken) throws InterruptedException, ExecutionException, IOException, URISyntaxException
    {
        disconnect();
        connection = client.open(new URI("ws://localhost:9000/app/message?auth_token=" + authToken), this).get();
        
    }
	
	public void send(String message) throws IOException
    {
		if(connection != null)
		{
	        connection.sendMessage(message);
	        sent.incrementAndGet();
		}
    }
	
	public void setClient(WebSocketClient client)
	{
		this.client = client;
	}

	@Override
	public void onMessage(byte[] arg0, int arg1, int arg2) {
		System.out.println(new String(arg0));	
	}
	
	public void addUserLoginEventListener(UserChangeEventListener listener)
	{
		loginListeners.add(listener);
	}
	
	public void addDisconnectEventListener(DisconnectEventListener listener)
	{
		disconnectListeners.add(listener);
	}

}
