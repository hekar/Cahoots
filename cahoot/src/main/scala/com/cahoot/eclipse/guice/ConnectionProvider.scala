package com.cahoot.eclipse.guice

import com.google.inject.Provider
import com.cahoot.client.connection.Connection

object ConnectionProvider {
  private var connection: Connection = null
  
  def getConnection(): Connection = {
    return connection
  }
  
}

class ConnectionProvider extends Provider[Connection] {
  
  def get(): Connection = {
    null
  }

}