package models

import play.api.libs.json.{JsNumber, JsString, JsObject}

class ActiveUser(var username: String, var name: String, var role: Int, var token: String, var status: String) {
  def toJson = {
    JsObject(
      Seq(
        "name"  -> JsString( username ),
        "status" -> JsString(status),
        "role" -> JsNumber(role)
      )
    )
  }
}