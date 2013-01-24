package models

import akka.actor._
import akka.util.duration._

import play.api._
import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.Play.current

import services._

import akka.util.Timeout
import akka.pattern.ask


/**
 * Reference:
 * https://github.com/playframework/Play20/blob/master/samples/scala/websocket-chat/app/models/ChatRoom.scala
 */
object MessageRelay {

  /**
   * Timeouts are 30 minutes long
   */
  implicit val timeout = Timeout(1800 second)

  lazy val default = {
    val messageActor = Akka.system.actorOf(Props[MessageRelay])
    messageActor
  }

  def join(username:String):Promise[(Iteratee[JsValue,_],Enumerator[JsValue])] = {
    (default ? Join(username)).asPromise.map {

      case Connected(enumerator) =>
        // Create an Iteratee to consume the feed
        val iteratee = Iteratee.foreach[JsValue] { event =>
          default ! Relay(username, event)
        }.mapDone { _ =>
          default ! Quit(username)
        }

        (iteratee,enumerator)

      case CannotConnect(error) => {
        // A finished Iteratee sending EOF
        val iteratee = Done[JsValue,Unit]((),Input.EOF)

        // Send an error and close the socket
        val enumerator =  Enumerator[JsValue](JsObject(Seq("error" -> JsString(error)))).andThen(Enumerator.enumInput(Input.EOF))

        (iteratee,enumerator)
      }
    }

  }
}

class MessageRelay extends Actor {

  var members = Map.empty[String, PushEnumerator[JsValue]]

  var services = Map[String, AsyncService](
            "users" -> new UsersService(this.notifyOne, this.notifyAll),
            "op" -> new OpService(this.notifyOne, this.notifyAll),
            "chat" -> new ChatService(this.notifyOne, this.notifyAll))

  def receive = {

    /*
     * Join
     * Called when a user first connects
     */
    case Join(username) => {
      // Create an Enumerator to write to this socket
      val channel =  Enumerator.imperative[JsValue]( onStart = self ! NotifyJoin(username))
      if(!members.contains(username)) {
        members = members + (username -> channel)
      }

      sender ! Connected(channel)
    }

    case NotifyJoin(username) => {
      this.services("users").asInstanceOf[UsersService].join(username)
    }

    case Relay(username, json) => {
      /*
       * This method is the main message relay center to pass the puck to other
       * classes which will handle things (operational transactions, chat, collabs, etc...)
       */

      val service = this.services((json \ "service").as[String])

      if (service != null) {
        service.processMessage(json)
      }
    }

    case Quit(username) => {
      // this should call UsersService.leave somehow...
      members = members - username
      this.services("users").asInstanceOf[UsersService].leave(username)
    }

  }

  def notifyAll(msg: JsValue) {
    members.foreach {
      case (_, channel) => channel.push(msg)
    }
  }

  def notifyOne(username: String, msg: JsValue) {
    val channel = members(username)
    channel.push(msg)
  }

}

case class Join(username: String)
case class Quit(username: String)
case class Relay(username: String, json: JsValue)
case class NotifyJoin(username: String)

case class Connected(enumerator:Enumerator[JsValue])
case class CannotConnect(msg: String)