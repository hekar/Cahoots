package controllers

import play.api._
import play.api.libs.json._
import play.cache._
import scala.collection.mutable._

import models._
import play.api.mvc._
import play.db.DB
import org.jooq.impl.Factory
import org.jooq.SQLDialect
import com.cahoots.jooq.tables.Users._

object Application extends Controller with Secured {

  def index = Action {
    Ok("")
  }

  def authenticate = Action { request =>
    val user = request.body.asFormUrlEncoded.get.get("username").get.apply(0)
    val pass = request.body.asFormUrlEncoded.get.get("password").get.apply(0)
    
    if(!check(user, pass))
    {
      Unauthorized("Invalid username/password")
    }

    var users:ListBuffer[ActiveUser] = Cache.get("users").asInstanceOf[ListBuffer[ActiveUser]]
    
    if(users == null)
    {
      users = new ListBuffer[ActiveUser]
      val c = DB.getConnection()
      val f = new Factory(c, SQLDialect.POSTGRES)
      val g = f.select(USERS.USERNAME).from(USERS).fetch(USERS.USERNAME).toArray.map(a => new ActiveUser(a.asInstanceOf[String], null))
      users.appendAll(g)
      Cache.set("users", users)
    }
    
    val stored_user = users.findIndexOf(x => x.username == user)
    
    if(stored_user != -1)
    {
      val token = java.util.UUID.randomUUID().toString()
      users(stored_user).token = token;
      Cache.set("users", users)
      Logger.info("User logged in with %s:%s".format(user, token))
      Ok(token)
    }
    else
    {
      Unauthorized("User not found")
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
      users(stored_user).token = null
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
