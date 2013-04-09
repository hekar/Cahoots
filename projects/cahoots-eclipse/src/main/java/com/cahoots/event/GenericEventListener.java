package com.cahoots.event;

public interface GenericEventListener<T> {
	void onEvent(T msg);
}
