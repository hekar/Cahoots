package com.cahoot.client.etherpad

class EtherpadClientException(message: String, t: Throwable) 
	extends RuntimeException(message, t) {

  def this() = {
    this("", null)
  }
  
  def this(message: String) = {
    this(message, null)
  }
  
}