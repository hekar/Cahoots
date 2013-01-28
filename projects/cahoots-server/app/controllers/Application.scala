package controllers

import play.api._
import play.api.libs.json._
import scala.collection.mutable._
import play.cache.Cache
import models._
import play.api.mvc._
import play.db.DB
import org.jooq.impl.Factory
import org.jooq.SQLDialect
import org.jooq.Record
import com.cahoots.jooq.tables.Users._
import com.cahoots.jooq.tables.Roles._
import scala.collection.JavaConversions._

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
    else
    {
      var users:ListBuffer[ActiveUser] = Cache.get("users").asInstanceOf[ListBuffer[ActiveUser]]

      if(users == null)
      {
        users = new ListBuffer[ActiveUser]
        val connection = DB.getConnection
        val create = new Factory(connection, SQLDialect.POSTGRES)
        for(r <- (create.select(USERS.USERNAME, USERS.NAME, ROLES.NAME).from(USERS).join(ROLES).on(ROLES.ID equal USERS.ROLE).fetch))
        {
          users.append(new ActiveUser(r.getValue(USERS.USERNAME), r.getValue(USERS.NAME), r.getValue(ROLES.NAME), null, "offline"))
        }
        Cache.set("users", users)
      }

      val stored_user = users.findIndexOf(x => x.username == user)

      val token = java.util.UUID.randomUUID().toString
      if(stored_user != -1)
      {
        users(stored_user).token = token
        Cache.set("users", users)
        Logger.info("User logged in with %s:%s".format(user, token))
        Ok(token)
      }
      else
      {
        users.append(new ActiveUser(user, user, "user", token, "offline"))
        Cache.set("users", users)
        Logger.info("User logged in with %s:%s".format(user, token))
        Ok(token)
      }
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
