package controllers

import views.html
import play.api.mvc.{Action, Controller}
import collection.mutable

object OpTransformation extends Controller with Secured {

  /**
   * Handle synchronization of the clock for an operational transformation session
   *
   * @return
   */
  def clock(auth_token: String, opId: String) = Action { request =>
    try {
      import services.OpSessions.ops

      val time = ops(opId).clock

      Ok(time.toString)
    }
    catch {
      case e: Exception =>
        Ok("No operational transformation session with clock open")
    }
  }
}
