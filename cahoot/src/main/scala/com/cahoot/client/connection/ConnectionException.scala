package com.cahoot.client.connection

class ConnectionException(message: String, cause: Throwable)
extends RuntimeException(message, cause) {

	def this() = {
		this("", null)
	}

	def this(message: String) = {
		this(message, null)
	}
}