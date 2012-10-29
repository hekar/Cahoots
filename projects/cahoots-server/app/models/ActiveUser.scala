package models

class ActiveUser(name: String, authToken: String) {
  var username: String = name
  var token: String = authToken
}