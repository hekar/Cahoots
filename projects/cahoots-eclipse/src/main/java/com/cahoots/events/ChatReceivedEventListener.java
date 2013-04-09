package com.cahoots.events;

import com.cahoots.serialize.json.receive.ChatReceiveMessage;

public interface ChatReceivedEventListener extends
		GenericEventListener<ChatReceiveMessage> {

}
