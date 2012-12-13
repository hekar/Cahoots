package services

import play.api.libs.json._
import collection.mutable.{ListBuffer, HashMap, LinkedList, HashSet => MutableHashSet}
import collection.mutable

/**
 * Operational transformations service
 */
class OpService(
                 sendOne: (String, JsValue) => Unit,
                 sendAll: (JsValue) => Unit)
  extends AsyncService("op", sendOne, sendAll) {

  /**
   * Document Id -> Shared operational transformation
   */
  val ops = new HashMap[String, OpSession]

  def processMessage(json: JsValue) {
    val t = (json \ "type").as[String]
    t match {
      case "share" =>
        val user = (json \ "user").as[String]
        val documentId = (json \ "documentId").as[String]
        val collaborators = (json \ "collaborators").as[List[String]]

        shareDocument(user, documentId, collaborators)
      case "unshare" =>
        val user = (json \ "user").as[String]
        val opId = (json \ "opId").as[String]
        val collaborators = (json \ "collaborators").as[List[String]]

        shareDocument(user, opId, collaborators)
      case "insert" =>
        val user = (json \ "user").as[String]
        val opId = (json \ "opId").as[String]
        val content = (json \ "content").as[String]
        val start = (json \ "start").as[Int]
        val tickStamp = (json \ "tickStamp").as[Long]

        insert(user, opId, content, start, tickStamp)
      case _ =>
        throw new RuntimeException("Invalid message type for 'op': %s".format(t))
    }
  }

  def shareDocument(user: String, documentId: String, collaborators: List[String]) {

    val nextOpSessionId = (if (ops.size > 0) ops.keys.map(_.toInt).toList.max else 0).toString

    /*
     * Always resend the notification, allow the client to deal with multiple notifications
     * on their end
     */
    val userJson = collaborators.map {
      collaborator =>
        (collaborator, JsObject(Seq(
          "service" -> JsString("op"),
          "type" -> JsString("shared"),
          "sharer" -> JsString(user),
          "documentId" -> JsString(documentId),
          "opId" -> JsString(nextOpSessionId)
        )))
    }

    // Send messages to each of the respective users
    userJson.foreach {
      user =>
        user match {
          case (u, json) =>
            notifyOne(u, json)
          case _ =>
          // Ignore..
        }
    }

    val session = new OpSession(nextOpSessionId.toInt, documentId, user)
    session.collaborators ++= collaborators
    ops += ((nextOpSessionId, session))
  }

  def unshareDocument(user: String, opId: String, collaborators: List[String]) {
    if (ops.contains(opId)) {
      val session = ops(opId)
      if (session.userHost == user) {

        // Notify the user and each of the collaborators
        (user :: collaborators).foreach {
          collaborator =>
            notifyOne(user, JsObject(Seq(
              "service" -> JsString("op"),
              "type" -> JsString("unshared"),
              "sharer" -> JsString(user),
              "opId" -> JsString(opId),
              "documentId" -> JsString(session.documentId)
            )))
        }

        ops -= (session.opSessionId.toString)
      }
    } else {
      notifyOne(user, JsObject(Seq(
        "service" -> JsString("op"),
        "type" -> JsString("nonExistentShared"),
        "message" -> JsString("Shared %s does not exist".format(opId))
      )))
    }
  }

  /**
   * Perform an operational insert
   *
   * @param user
   * User that performed the operation
   * @param opId
   * Op session identifier
   * @param content
   * Content of the insertion
   * @param start
   * Index at which the content was inserted
   * @param tickStamp
   * The tickstamp that the message was created at
   */
  def insert(user: String, opId: String, content: String, start: Int, tickStamp: Long) {
    if (ops.contains(opId)) {
      val opSession = ops(opId)

      val validUser = opSession.collaborators.contains(user)

      if (validUser) {
        /*
         * Notify all clients of this operational transformation that an insert
         * operation has been performed
         */
        opSession.collaborators.foreach { collaborator =>
          notifyOne(collaborator, JsObject(Seq(
            "service" -> JsString("op"),
            "type" -> JsString("insert"),
            "by" -> JsString(user),
            "opId" -> JsString(opId),
            "documentId" -> JsString(opSession.documentId)
          )))
        }

      } else {
        notifyOne(user, JsObject(Seq(
          "service" -> JsString("op"),
          "type" -> JsString("invalidUserOnShare"),
          "message" -> JsString("User %s does not exist in share %s".format(user, opId))
        )))
      }

    } else {
      notifyOne(user, JsObject(Seq(
        "service" -> JsString("op"),
        "type" -> JsString("nonExistentShared"),
        "message" -> JsString("Shared %s does not exist".format(opId))
      )))
    }
  }


}

/**
 * A shared operational transformation session
 */
class OpSession(val opSessionId: Int, val documentId: String, val userHost: String) {

  /**
   * Collaborator user ids
   */
  val collaborators = new MutableHashSet[String]

}