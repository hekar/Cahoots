package com.cahoot.eclipse.guice

import com.google.inject.Provider

object ConnectionProvider {
  private var connection: Connection = null
  
  def getConnection(): Connection = {
    return connection
  }
  
}

class ConnectionProvider extends Provider[Connection] {
  
  def get(): Connection = {
    connection
  }

}