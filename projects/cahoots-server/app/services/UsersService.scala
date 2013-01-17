package services


import play.api.libs.json._
import models.ActiveUser
import collection.mutable.ListBuffer
import play.cache.Cache


class UsersService(
        sendOne: (String, JsValue) => Unit,
        sendAll: (JsValue) => Unit)
    extends AsyncService("users", sendOne, sendAll){

  def processMessage(json: JsValue) {
  }

  def join(username: String) {
    val users:ListBuffer[ActiveUser] = Cache.get("users").asInstanceOf[ListBuffer[ActiveUser]]
    val user = users.filter(t => t.username == username)(0)
    user.status = "online"
    Cache.set("users", users)
    val objects = new ListBuffer[JsObject]
    for (user <- users)
    {
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
    val users:ListBuffer[ActiveUser] = Cache.get("users").asInstanceOf[ListBuffer[ActiveUser]]
    val user = users.filter(t => t.username == username)(0)
    user.status = "offline"
    Cache.set("users", users)

    notifyAll(JsObject(
      Seq(
        "service" -> JsString("users"),
        "type" -> JsString("status"),
        "user" -> (user toJson)
      )
    ))
  }

}