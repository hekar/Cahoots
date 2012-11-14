package models

import akka.actor._
import akka.util.duration._

import play.api._
import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.concurrent._

import akka.util.Timeout
import akka.pattern.ask

import play.api.Play.current


object MessageRelay {
  
  implicit val timeout = Timeout(1 second)
  
  lazy val default = {
    val messageActor = Akka.system.actorOf(Props[MessageRelay])
    
    messageActor
  }

  def join(auth_token:String):Promise[(Iteratee[JsValue,_],Enumerator[JsValue])] = {
    (default ? Join(auth_token)).asPromise.map {
      
      case Connected(enumerator) => 
      
        // Create an Iteratee to consume the feed
        val iteratee = Iteratee.foreach[JsValue] { event =>
          default ! Talk(auth_token, (event \ "text").as[String])
        }.mapDone { _ =>
          default ! Quit(auth_token)
        }
        
        Logger.info(auth_token)

        (iteratee,enumerator)
        
      case CannotConnect(error) => 
      
        // Connection error

        // A finished Iteratee sending EOF
        val iteratee = Done[JsValue,Unit]((),Input.EOF)

        // Send an error and close the socket
        val enumerator =  Enumerator[JsValue](JsObject(Seq("error" -> JsString(error)))).andThen(Enumerator.enumInput(Input.EOF))
        
        (iteratee,enumerator)
         
    }

  }
  
}

class MessageRelay extends Actor {
  
  var members = Map.empty[String, PushEnumerator[JsValue]]
  
  def receive = {
    case Join(auth_token) => {
      // Create an Enumerator to write to this socket
      val channel =  Enumerator.imperative[JsValue]( onStart = self ! NotifyJoin(auth_token))
      if(members.contains(auth_token)) {
        sender ! CannotConnect("This username is already used")
      } else {
        members = members + (auth_token -> channel)
        sender ! Connected(channel)
      }
    }

    case NotifyJoin(auth_token) => {
      /*
       * This method is going to be rewritten to send a message to everyone and be like
       * "USER HEKAR IS ONLINE!"
       */
      notifyAll("join", auth_token, "has entered the room")
    }
    
    case Talk(auth_token, text) => {
      /*
       * This method will be the main message relay center to pass the puck
       * to other classes which will handle things (operational transactions, chat, collabs, etc...)
       */
      notifyAll("talk", auth_token, text)
    }
    
    case Quit(auth_token) => {
      /*
       * This method will send a message like "USER SAMWISE IS NOW OFFLINE"
       */
      members = members - auth_token
      notifyAll("quit", auth_token, "has leaved the room")
    }
    
  }
  
  def notifyAll(kind: String, user: String, text: String) {
    val msg = JsObject(
      Seq(
        "kind" -> JsString(kind),
        "user" -> JsString(user),
        "message" -> JsString(text),
        "members" -> JsArray(
          members.keySet.toList.map(JsString)
        )
      )
    )
    members.foreach { 
      case (_, channel) => channel.push(msg)
    }
  }
  
}

case class Join(username: String)
case class Quit(username: String)
case class Talk(username: String, text: String)
case class NotifyJoin(username: String)

case class Connected(enumerator:Enumerator[JsValue])
case class CannotConnect(msg: String)
