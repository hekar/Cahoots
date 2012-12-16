package controllers

import play.db.DB
import play.api.Play._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import org.jooq.SQLDialect
import org.jooq.impl.Factory
import com.cahoots.jooq.tables.Users._
import java.security.MessageDigest

object Auth extends Controller with Secured {

  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying ("Invalid username or password", result => result match {
      case (username, password) => check(username, password)
    })
  )

  def check(username: String, password: String) = {
    val c = DB.getConnection()
    val f = new Factory(c, SQLDialect.POSTGRES)
    val pass = hash(password)
    val result = f.select(USERS.USERNAME).from(USERS).where( USERS.USERNAME equal username).and(USERS.PASSWORD equal pass).fetch()
    (result.size() == 1)
  }

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  def checkCredentials = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.Portal.index).withSession("username" -> user._1)
    )
  }

  def logout = Action {
    Redirect(routes.Auth.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }

}

/**
 * Provide security features
 */
trait Secured {

  private def username(request: RequestHeader) = request.session.get("username")
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Auth.login)

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) =
    Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }

  def hash(text: String) : String = {
    MessageDigest.getInstance("MD5").digest(text.getBytes).map("%02x" format _).mkString
  }
}