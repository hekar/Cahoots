package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def authenticate = Action { request =>
    val user = request.body.asFormUrlEncoded.get.get("username").get.apply(0)
    val pass = request.body.asFormUrlEncoded.get.get("password").get.apply(0)
    
    // TODO: do actual authenticaty stuff here...
    var token = "$AuthTokenGoesHere"
    //Ok(token)
    Forbidden("Invalid username/password")
  }
}
