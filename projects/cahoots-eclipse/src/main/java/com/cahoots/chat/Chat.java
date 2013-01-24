package com.cahoots.chat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
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
	public Chat(CahootsSocket socket) {
		socket.addChatReceivedEventListener(new ChatReceivedEventListener() {
			@Override
			public void onEvent(ChatReceiveMessage msg) {
				ChatDialog dialog;
				if (!chats.containsKey(msg.getFrom())) {
					Shell parent = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell();
					List<String> collaborators = Arrays.asList(msg.getFrom());
					dialog = new ChatDialog(parent, collaborators);
				} else {
					dialog = chats.get(msg.getFrom());
				}
				dialog.receiveMessage(msg);
				dialog.open();
			}
		});
	}

	public void startChat(String to) {
		ChatDialog dialog;
		if (!chats.containsKey(to)) {
			Shell parent = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell();
			List<String> collaborators = Arrays.asList(to);
			dialog = new ChatDialog(parent, collaborators);
		} else {
			dialog = chats.get(to);
		}
		chats.put(to, dialog);
		dialog.open();
	}
}
