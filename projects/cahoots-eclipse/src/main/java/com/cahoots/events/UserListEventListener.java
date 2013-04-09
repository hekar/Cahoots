package com.cahoots.events;

import com.cahoots.serialize.json.receive.UserListMessage;

public interface UserListEventListener extends
		GenericEventListener<UserListMessage> {

}
