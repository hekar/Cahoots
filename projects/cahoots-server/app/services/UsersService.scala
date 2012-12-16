package services

import akka.actor._
import akka.util.duration._

import play.api._
import libs.json.JsArray
import libs.json.JsObject
import libs.json.JsString
import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.concurrent._

import akka.util.Timeout
import akka.pattern.ask

import play.api.Play.current
import models.{ActiveUser, MessageRelay}
import collection.mutable.ListBuffer
import play.db.DB
import org.jooq.impl.Factory
import org.jooq.SQLDialect
import com.cahoots.jooq.tables.Users._


class UsersService(
        sendOne: (String, JsValue) => Unit,
        sendAll: (JsValue) => Unit)
    extends AsyncService("users", sendOne, sendAll){

  var users:Map[String,String] = {

    var ma = new collection.mutable.HashMap[String,String]
    val c = DB.getConnection()
    val f = new Factory(c, SQLDialect.POSTGRES)
    for( x <-f.select(USERS.USERNAME).from(USERS).fetch(USERS.USERNAME).toArray){
      ma.put(x.asInstanceOf[String], "offline")
    }
    ma.toMap
  }

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
    users = users + (user -> "offline")

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
