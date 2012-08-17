package models.collaborator

import anorm._
import play.api.Play._
import play.api.db._

object GroupModel {
  def fetchByName(user: String, name: String) = {
    DB.withConnection { implicit c =>
      
    }
  }
  
  def fetchByUser(user: String) = {
    DB.withConnection { implicit c =>
    }
  }
  
  def fetchByGuid(guid: String) = {
    DB.withConnection { implicit c =>
    }
  }
  
}

class GroupModel(user: String, guid: String, name: String, collabs: List[String])
