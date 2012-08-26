package com.cahoot.client.mock

import com.cahoot.client.connection.Connection

class MockConnection(address: String, port: Int) extends Connection(address, port) {
  var opened = false

  override def open(): Unit = {
    opened = true
  }

  override def close(): Unit = {
    assert(opened == true)
  }

}
