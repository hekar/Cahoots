package com.cahoots.chat;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.PlatformUI;

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.ChatDialog;
import com.cahoots.events.ChatReceivedEventListener;
import com.cahoots.json.receive.ChatReceiveMessage;
import com.google.inject.Inject;

public class Chat {
	
	private Map<String, ChatDialog> chats = new HashMap<String, ChatDialog>();
	
	@Inject
	public Chat(CahootsSocket socket)
	{
		socket.addChatReceivedEventListener(new ChatReceivedEventListener() {
			
			@Override
			public void onEvent(ChatReceiveMessage msg) {
				ChatDialog dia;
				if(!chats.containsKey(msg.getFrom()))
				{
					dia = new ChatDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), msg.getFrom());
				} else 
				{
					dia = chats.get(msg.getFrom());
				}
				dia.receiveMessage(msg);
				dia.open();
			}
		});
	}
	
	public void startChat(String to)
	{
		ChatDialog dia;
		if(!chats.containsKey(to))
		{
			dia = new ChatDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), to);
		} 
		else 
		{
			dia = chats.get(to);
		}
		chats.put(to, dia);
		dia.open();
	}
}
