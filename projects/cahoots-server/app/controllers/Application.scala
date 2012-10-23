package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok("roffles")
  }
  
  def login(username: String, password: String) = Action {
    Ok("")
  }
}
