package com.cahoot.client.connection

/**
 * Connection to the cahoot server
 */
class Connection(
  val address: String,
  val port: Int) {

  /**
   * Blocking version. Open a connection
   */
  def open(): Unit = {

  }

  /**
   * Non-blocking version. Open a connection
   */
  def open(onSuccess: (Connection) => Unit, onFailure: (Throwable) => Unit) = {

  }

  /**
   * Close the connection
   */
  def close(): Unit = {

  }

  /**
   * Test to make sure the connection works
   *
   * @param onSuccess
   * 	On success
   * @param onFailure
   */
  def test(onSuccess: (Connection) => Unit, onFailure: (Throwable) => Unit): Unit = {
  }

  /**
   * <p>
   * Blocking version of write
   * </p>
   *
   * <p>
   * Write pieces of data to be serialized to the client as a request
   * </p>
   *
   * @param obj
   * 	Any sequence of objects. They will be passed in order of argument to the client.
   */
  def write(obj: Any*): Unit = {

  }

  /**
   * Non-Blocking version of write
   */
  def write(): Unit = {

  }

  /**
   * Blocking version of read
   */
  def read(): Any = {

  }

  /**
   * Non-blocking version of read
   */
  def read(onRead: (Any)): Unit = {

  }

}