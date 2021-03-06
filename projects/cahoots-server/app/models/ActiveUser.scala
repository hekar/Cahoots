package models

import play.api.libs.json.{JsString, JsObject}

class ActiveUser(var username: String, var name: String, var role: String, var token: String, var status: String, var id: Integer = null) {
  def toJson = {
    JsObject(
      Seq(
        "username"  -> JsString( username ),
        "status" -> JsString(status),
        "role" -> JsString(role),
        "name" -> JsString(name)
      )
    )
  }
}