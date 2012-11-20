package services

import akka.actor._
import akka.util.duration._

import play.api._
import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.concurrent._

import akka.util.Timeout
import akka.pattern.ask

import play.api.Play.current
import models.MessageRelay
import collection.mutable.ListBuffer


class UsersService(
        sendOne: (String, JsValue) => Unit,
        sendAll: (JsValue) => Unit)
    extends AsyncService("users", sendOne, sendAll){

  var users = Map.empty[String, String]

  def processMessage(json: JsValue) {
  }

  def join(user: String) {
    users = users + (user -> "online")

    val objects = new ListBuffer[JsObject]

    users.foreach {
      case (a, b) => {
        objects.append(JsObject(
          Seq(
            "name" -> JsString(a),
            "status" -> JsString(b)
          )))
      }
    }

    notifyOne(
      user,
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
        "user" -> JsObject(
          Seq(
            "name"  -> JsString( user ),
            "status" -> JsString("online"),
            "role" -> JsString("none")
          )
        )
      )
    ))
  }

  def leave(user: String) {
    users = users - user

    notifyAll(JsObject(
      Seq(
        "service" -> JsString("users"),
        "type" -> JsString("status"),
        "user" -> JsObject(
          Seq(
            "name"  -> JsString( user ),
            "status" -> JsString("offline"),
            "role" -> JsString("none")
          )
        )
      )
    ))
  }

}
