package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def authenticate = Action { request =>
    val user = request.body.asFormUrlEncoded.get.get("username").get.apply(0)
    val pass = request.body.asFormUrlEncoded.get.get("password").get.apply(0)
    
    // TODO: do actual authenticaty stuff here...
    var token = "$AuthTokenGoesHere"
      
    if (token == null)
    {
      Forbidden("Invalid username/password")
    }
    else
    {
      Ok(token)
    }
  }
  
  def deauthenticate = Action { request =>
    val token = request.body.asFormUrlEncoded.get.get("token").get.apply(0)
    
    // TODO: do actual deauthentication
    Ok("")
    Forbidden("Unrecognized requst token")
  }
}
