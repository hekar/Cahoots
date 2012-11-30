package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.iteratee._
import play.cache._
import scala.collection.mutable._

import views._
import models._

object Application extends Controller {

  def index = Action {
    Ok("")
  }

  def authenticate = Action { request =>
    val user = request.body.asFormUrlEncoded.get.get("username").get.apply(0)
    val pass = request.body.asFormUrlEncoded.get.get("password").get.apply(0)
    
    // TODO: do actual authenticaty stuff here...
    var users:ListBuffer[ActiveUser] = Cache.get("users").asInstanceOf[ListBuffer[ActiveUser]]
    
    if(users == null)
    {
      users = new ListBuffer[ActiveUser]
    }
    
    var stored_user = users.findIndexOf(x => x.username == user)
    
    if(stored_user != -1)
    {
      users.remove(stored_user)
    }
    
    var token:String = Cache.get(user).asInstanceOf[String]
    
    if (token == null)
    {
      token = java.util.UUID.randomUUID().toString()
      Cache.set(user, token)
    }
      
    if (token != null)
    {
      users.append(new ActiveUser(user, token))
      Cache.set("users", users)
      Logger.info("User logged in with %s:%s".format(user, token))
      Ok(token)
    }
    else
    {
      Unauthorized("Invalid username/password")
    }
    
  }
  
  def deauthenticate = Action { request =>
    val token = request.body.asFormUrlEncoded.get.get("auth_token").get.apply(0)
    
    var users:ListBuffer[ActiveUser] = Cache.get("users").asInstanceOf[ListBuffer[ActiveUser]]
    
    if(users == null)
    {
      users = new ListBuffer[ActiveUser]
    }
    
    val stored_user = users.findIndexOf(x => x.token == token)

    if(stored_user != -1)
    {
      Logger.info("User logged out with %s".format(token))
      val username = users(stored_user).username
      Cache.set(username, null) //TODO is this used anywhere else?
      users.remove(stored_user)
    }
    Cache.set("users", users)

    Ok("")
  }
  
  /**
   * Handles the websocket connection.
   */
  def message(auth_token: String) = WebSocket.async[JsValue] { request  =>
    val users:ListBuffer[ActiveUser] = Cache.get("users").asInstanceOf[ListBuffer[ActiveUser]]
    val stored_user = users.findIndexOf(x => x.token == auth_token)

    if (stored_user == -1) {
      throw new Exception("Not Validated")
    }

    val user = users(stored_user)

    MessageRelay.join(user.username)
  }
}
