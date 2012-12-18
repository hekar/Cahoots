package controllers


import play.api.Play._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import play.db.DB
import org.jooq.impl.Factory
import org.jooq.SQLDialect
import com.cahoots.jooq.tables.Users._
import com.cahoots.jooq.tables.Roles._

object Portal extends Controller with Secured {

  val createUserForm = Form(
    tuple(
      "username" -> text,
      "password" -> text,
      "email" -> email,
      "role" -> text
    ).verifying ("Failed to create user", result => result match {
      case (username, password, email, role) => create(username, password, email, role)
    })
  )

  def index = IsAuthenticated { username => implicit request =>
    Ok(html.portal.index(createUserForm, roles))
  }

  def createUser = IsAuthenticated { username => implicit request =>
    createUserForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.portal.index(createUserForm, roles )),
      user => Redirect(routes.Auth.login)
    )
  }

  def roles= {
    val c = DB.getConnection
    val f = new Factory(c, SQLDialect.POSTGRES)
    f.select(ROLES.NAME).from(ROLES).fetch().getValues(ROLES.NAME)

  }

  def create(username: String, password: String, email:String, role:String) : Boolean= {

    val c = DB.getConnection
    val f = new Factory(c, SQLDialect.POSTGRES)
    val pass = hash(password)
    val roleId = f.select(ROLES.ID).from(ROLES).where(ROLES.NAME equal role).fetchOne(ROLES.ID)

    f.insertInto(USERS, USERS.PASSWORD, USERS.NAME, USERS.USERNAME, USERS.ROLE)
      .values(pass, username, username, roleId).execute()

    (-4  == 1)
  }

}