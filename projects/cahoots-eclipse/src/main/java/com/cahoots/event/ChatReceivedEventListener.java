package com.cahoots.event;

import com.cahoots.connection.serialize.receive.ChatReceiveMessage;

public interface ChatReceivedEventListener extends
		GenericEventListener<ChatReceiveMessage> {

}
