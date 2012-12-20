package controllers

import play.api._
import play.api.mvc._
import play.api.Play._
import play.api.libs.json._
import play.cache._

import collection.mutable._
import models._

object User extends Controller with Secured {
  def listUsers(auth_token: String) = Action {
    request =>
      val users: ListBuffer[ActiveUser] = Cache.get("users").asInstanceOf[ListBuffer[ActiveUser]]
      val stored_user = users.findIndexOf(x => x.token == auth_token)

      if (stored_user == -1) {
        throw new Exception("Not Validated")
      }

      val userInfos = users.map({
        user =>
          JsString(user.username)
      }).toSeq

      val json = Json.stringify(JsObject(Seq(
        "users" -> JsArray(userInfos)
      )))

      Ok(json).as("application/json")
  }
}
