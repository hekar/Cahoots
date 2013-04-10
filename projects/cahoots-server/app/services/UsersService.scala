package services


import play.api.libs.json._
import models.{MessageRelay, ActiveUser}
import collection.mutable.ListBuffer

object UsersService {
    val users = ListBuffer[ActiveUser]()
}

class UsersService(
        sendOne: (String, JsValue) => Unit,
        sendAll: (JsValue) => Unit)
    extends AsyncService("users", sendOne, sendAll){

    import UsersService._

  def processMessage(json: JsValue) {
  }

  def join(username: String) {
    val user = users.filter(t => t.username == username)(0)
    user.status = "online"
    val objects = new ListBuffer[JsObject]
    for (user <- users) {
      objects.append(user toJson)
    }

    notifyOne(
      user.username,
      JsObject(
        Seq(
          "service" -> JsString("users"),
          "type" -> JsString("all"),
          "users" -> JsArray(objects)
        ))
    )

    notifyAll(JsObject(
      Seq(
        "service" -> JsString("users"),
        "type" -> JsString("status"),
        "user" -> (user toJson)
      )
    ))
  }

  def leave(username: String) {

    MessageRelay.services("op").asInstanceOf[OpService].leaveAll(username)

    val user = users.filter(t => t.username == username)(0)
    user.status = "offline"

    notifyAll(JsObject(
      Seq(
        "service" -> JsString("users"),
        "type" -> JsString("status"),
        "user" -> (user toJson)
      )
    ))
  }

}