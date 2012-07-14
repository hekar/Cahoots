package com.cahoot.models
import com.cahoot.client.connection.Connection

/**
 * Serializable model to hold a store to a connection
 */
class ConnectionModel(
  val address: String,
  val port: Int) {

  def open(): Connection = {
    val connection = new Connection(address, port)

    connection.open

    connection
  }

  /**
   * Test a connection to the server
   */
  def test(): Unit = {

  }

}