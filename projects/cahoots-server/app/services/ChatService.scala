package services

import play.api.libs.json._
import play.db.DB
import org.jooq.impl.Factory
import org.jooq.SQLDialect
import com.cahoots.jooq.tables.Chat._

/**
 * Created with IntelliJ IDEA.
 * User: T
 * Date: 1/16/13
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */
class ChatService(
                   sendOne: (String, JsValue) => Unit,
                   sendAll: (JsValue) => Unit)
  extends AsyncService("chat", sendOne, sendAll) {

  /*
   * Processes messages or something..
   */
  def processMessage(json: JsValue) {
    val msgType :String = (json \ "type").as[String];



    if (msgType == "send") {
      sendMessage(json);
    }
  }

  def sendMessage(json: JsValue) {
    val to = (json \ "to").as[String];
    val from = (json \ "from").as[String];
    val message = (json \ "message").as[String];
    val time = (json \ "timestamp").as[String];


    val c = DB.getConnection()
    val f = new Factory(c, SQLDialect.POSTGRES)
    if (f.insertInto(CHAT, CHAT.FROM, CHAT.TO, CHAT.DATE, CHAT.MESSAGE).values(from, to, time, message).execute() != 0)
    {
      notifyOne(to,
        JsObject(
          Seq(
            "from" -> JsString(from),
            "message" -> JsString(message),
            "timestamp" -> JsString(time),
            "type" -> JsString("receive"),
            "service" -> JsString("chat")
          ))
      )
    }
  }
}
