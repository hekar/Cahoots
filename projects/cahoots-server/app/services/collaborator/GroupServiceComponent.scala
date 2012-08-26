package services.collaborator

import play.api.libs.json.Json.toJson
import play.api.libs.json.JsValue

/**
 * Services for a group
 */
trait GroupServiceComponent {

  protected val groupService: GroupService = new GroupServiceImpl()

  trait GroupService {
    def createGroup(json: JsValue): JsValue;
    def editGroup(json: JsValue): JsValue;
    def archiveGroup(json: JsValue): JsValue;
    def listGroups(json: JsValue): JsValue;
    def getGroup(json: JsValue): JsValue;
  }

  private class GroupServiceImpl extends GroupService {
    /**
     * Add a group for the user
     *
     * Parameters:
     * 	name: String
     * 		Name of group to create
     * 	collabs: List[String]
     * 		Guids of collaborators
     *
     * Exceptions:
     * 	Name must be unique
     */
    def createGroup(json: JsValue) = {
      val name = (json \ "name").as[String]
      val collabs = (json \ "collabs").as[List[String]]

      // TODO: Check if name is unique

      // TODO: Check guids of collaborators

      toJson(
        Map("status" -> "OK",
          "message" -> "%s created".format(name))
      )
    }

    /**
     * Edit a group's collaborators and/or name
     *
     * Parameters:
     * 	name: String
     * 		Name of group to create
     * 	collabs: List[String]
     * 		Guids of collaborators
     *
     * Exceptions:
     * 	Name must be unique
     * 	Group does not exist
     * 	Group archived
     */
    def editGroup(json: JsValue) = {
      val name = (json \ "name").as[String]
      val newName = (json \ "newName").as[String]
      val collabs = (json \ "collabs").as[List[String]]

      // TODO: Check if name exists

      // TODO: Check if new name is unique

      // TODO: Check guids of collaborators

      toJson(
        Map("status" -> "OK",
          "message" -> "%s edited".format(name))
      )
    }

    /**
     * Move a group to the archive
     *
     * Parameters:
     * 	name: String
     * 		Name of group to delete
     *
     * Exception:
     * 	Group cannot be deleted
     * 	Group does not exist
     * 	Group has already been archived
     */
    def archiveGroup(json: JsValue) = {
      val name = (json \ "name")

      toJson(
        Map("status" -> "OK",
          "message" -> "%s Deleted".format(name))
      )
    }

    /**
     * List the user's groups
     */
    def listGroups(json: JsValue) = {
      // List the user's groups

      val groups = Seq("Bob")

      toJson(
        Map("status" -> toJson("OK"),
          "message" -> toJson("Groups Listed"),
          "groups" -> toJson(groups)
        )
      )
    }
    
    /**
     * Get the desired group's details
     * 
     * Parameters:
     * 	name: String
     * 		Name of group
     *
     * Exceptions:
     * 	Group does not exist	
     */
    def getGroup(json: JsValue) = {
      
      val groups = Seq("Bob")

      toJson(
        Map("status" -> toJson("OK"),
          "message" -> toJson("Groups Listed"),
          "groups" -> toJson(groups)
        )
      )
 
    }
  }
}