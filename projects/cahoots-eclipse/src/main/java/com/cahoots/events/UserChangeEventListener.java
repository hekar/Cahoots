package com.cahoots.events;

import com.cahoots.serialize.json.receive.UserChangeMessage;

public interface UserChangeEventListener extends
		GenericEventListener<UserChangeMessage> {
}
