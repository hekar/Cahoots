package models

import play.api.libs.json.{JsString, JsObject}

class ActiveUser(var username: String, var name: String, var role: String, var token: String, var status: String) {
  def toJson = {
    JsObject(
      Seq(
        "name"  -> JsString( username ),
        "status" -> JsString(status),
        "role" -> JsString(role)
      )
    )
  }
}