package controllers.collaborator

import play.api._
import play.api.mvc._
import play.api.libs.json.JsValue
import services.collaborator.GroupServiceComponent

/**
 * Contains a group of collaborators
 */
object Group extends Controller with GroupServiceComponent {

  /**
   * Create a new group of collaborators
   *
   *
   * Groups pertain to the authenticated user
   * and the authenticated user only. They are
   * visible by no one else.
   */
  def createGroup = Action(parse.json) { request =>
    Ok(groupService.createGroup(request.body))
  }

  /**
   * Archive a group in the database
   */
  def archiveGroup = Action(parse.json) { request =>
    Ok(groupService.archiveGroup(request.body))
  }

}