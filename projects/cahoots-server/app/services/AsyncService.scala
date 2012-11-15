package services

import play.api.libs.json._

abstract class AsyncService(
                    val serviceName: String,
                    val sendOne: (String, JsValue) => Unit,
                    val sendAll: (JsValue) => Unit) extends AsyncTrait {

  def notifyAll(msg: JsValue) {
    if (sendOne == null)
    {
      throw new Exception("Service not configured to send to all")
    }

    sendAll(msg)
  }

  def notifyOne(user: String, msg: JsValue) {
    if (sendOne == null)
    {
      throw new Exception("Service not configured to send to one")
    }

    sendOne(user, msg)
  }

}

trait AsyncTrait {

  def processMessage(json: JsValue)
  def notifyAll(msg: JsValue)
  def notifyOne(user: String, msg: JsValue)
}