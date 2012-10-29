package controllers

import play.api._
import play.api.mvc._
import play.cache._

object Application extends Controller {
  
  def authenticate = Action { request =>
    val user = request.body.asFormUrlEncoded.get.get("username").get.apply(0)
    val pass = request.body.asFormUrlEncoded.get.get("password").get.apply(0)
    
    // TODO: do actual authenticaty stuff here...
    
    
    var token:String = Cache.get(user + pass).asInstanceOf[String]
    
    if (token == null)
    {
      token = java.util.UUID.randomUUID().toString()
      Cache.set(user + pass, token)
    }
      
    if (token != null)
    {
      
      Ok(token)
    }
    else
    {
      Unauthorized("Invalid username/password")
    }
  }
  
  def deauthenticate = Action { request =>
    val token = request.body.asFormUrlEncoded.get.get("token").get.apply(0)
    
    // TODO: do actual deauthentication
    Ok("")
    //Unauthorized("Unrecognized request token")
  }
}
