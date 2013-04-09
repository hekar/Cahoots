package com.cahoots.event;

import com.cahoots.connection.serialize.receive.UserChangeMessage;

public interface UserChangeEventListener extends
		GenericEventListener<UserChangeMessage> {
}
