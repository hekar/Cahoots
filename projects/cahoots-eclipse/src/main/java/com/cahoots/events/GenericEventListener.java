package com.cahoots.events;

public interface GenericEventListener<T> {
	void onEvent(T msg);
}
