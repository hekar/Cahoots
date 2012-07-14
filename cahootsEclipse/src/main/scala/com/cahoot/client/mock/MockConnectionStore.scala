package com.cahoot.client.mock

import com.cahoot.client.connection.ConnectionStore
import com.cahoot.models.ConnectionModel

class MockConnectionStore extends ConnectionStore {
  override def listConnections(): List[ConnectionModel] = {
    List(new ConnectionModel("localhost", 80))
  }
}