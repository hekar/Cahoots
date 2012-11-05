package controllers

import play.api._
import play.api.mvc._

import views._

object Portal extends Controller with Secured {

  def index = IsAuthenticated { username => implicit request =>
    Ok(html.portal.index())
  }

}