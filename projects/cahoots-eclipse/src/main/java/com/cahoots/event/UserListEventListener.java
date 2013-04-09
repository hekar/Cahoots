package com.cahoots.event;

import com.cahoots.connection.serialize.receive.UserListMessage;

public interface UserListEventListener extends
		GenericEventListener<UserListMessage> {

}
