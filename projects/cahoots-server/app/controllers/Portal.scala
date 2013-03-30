package controllers


import play.api.Play._
import play.api.data._
import views._
import scala.collection.mutable._
import models._
import play.api.mvc._
import play.db.DB
import org.jooq.impl.Factory
import org.jooq.SQLDialect
import com.cahoots.jooq.tables.Users._
import com.cahoots.jooq.tables.Roles._
import scala.collection.JavaConversions._

import play.api._
import data.Forms.email
import data.Forms.text
import data.Forms.tuple

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

    val c = DB.getConnection
    val f = new Factory(c, SQLDialect.POSTGRES)

    val users = new ListBuffer[ActiveUser]
    for(r <- (f.select(USERS.ID, USERS.USERNAME, USERS.NAME, ROLES.NAME).from(USERS).join(ROLES).on(ROLES.ID equal USERS.ROLE).fetch))
    {
      users.append(new ActiveUser(r.getValue(USERS.USERNAME), r.getValue(USERS.NAME), r.getValue(ROLES.NAME), null, "offline", r.getValue(USERS.ID)))
    }
    Ok(html.portal.index(createUserForm, roles, users))
  }

  def roles= {
    val c = DB.getConnection
    val f = new Factory(c, SQLDialect.POSTGRES)
    f.select(ROLES.NAME).from(ROLES).fetch().getValues(ROLES.NAME)

  }


  def deleteUser(id: Int) = Action {
    delete(id)
    Redirect(routes.Portal.index)
  }

  def delete(id: Int): Boolean = {
    val c = DB.getConnection
    val f = new Factory(c, SQLDialect.POSTGRES)
  
    f.delete(USERS).where(USERS.ID equal id ).execute() == 1
  }

  def create(username: String, password: String, email:String, role:String) : Boolean= {

    val c = DB.getConnection
    val f = new Factory(c, SQLDialect.POSTGRES)
    val pass = hash(password)
    val roleId = f.select(ROLES.ID).from(ROLES).where(ROLES.NAME equal role).fetchOne(ROLES.ID)

    f.insertInto(USERS, USERS.PASSWORD, USERS.NAME, USERS.USERNAME, USERS.ROLE)
      .values(pass, username, username, roleId).execute()

    false
  }

}