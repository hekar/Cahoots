package services

import play.api._
import play.api.libs.json._
import collection.mutable.{HashMap, HashSet => MutableHashSet}
import collection.mutable

/**
 * Operational transformations service
 */
class OpService(
                 sendOne: (String, JsValue) => Unit,
                 sendAll: (JsValue) => Unit)
  extends AsyncService("op", sendOne, sendAll) {

  import OpSessions.ops

  def processMessage(json: JsValue) {
    Logger.debug("Message received: ")
    Logger.debug(json.toString())

    val t = (json \ "type").as[String]
    t match {
      case "share" =>
        val user = (json \ "user").as[String]
        val documentId = (json \ "documentId").as[String]
        val collaborators = (json \ "collaborators").as[List[String]]
        val fileContents = (json \ "fileContents").as[String]
        shareDocument(user, documentId, collaborators, fileContents)
      case "insert" =>
        val user = (json \ "user").as[String]
        val opId = (json \ "opId").as[String]
        val content = (json \ "content").as[String]
        val start = (json \ "start").as[Int]
        val tickStamp = (json \ "tickStamp").as[Long]

        insert(user, opId, content, start, tickStamp)
      case "replace" =>
        val user = (json \ "user").as[String]
        val opId = (json \ "opId").as[String]
        val content = (json \ "content").as[String]
        val start = (json \ "start").as[Int]
        val end = (json \ "end").as[Int]
        val tickStamp = (json \ "tickStamp").as[Long]

        replace(user, opId, content, start, end, tickStamp)
      case "delete" =>
        val user = (json \ "user").as[String]
        val opId = (json \ "opId").as[String]
        val start = (json \ "start").as[Int]
        val end = (json \ "end").as[Int]
        val tickStamp = (json \ "tickStamp").as[Long]

        delete(user, opId, start, end, tickStamp)
      case "leave" =>
        val user = (json \ "user").as[String]
        val opId = (json \ "opId").as[String]

        leave(user, opId)

      case "join" =>
        val user = (json \ "user").as[String]
        val opId = (json \ "opId").as[String]

        join(user, opId)

      case "invite" =>
        val user = (json \ "user").as[String]
        val opId = (json \ "opId").as[String]
        val sharer = (json \ "sharer").as[String]

        invite(sharer, user, opId)

      case _ =>
        throw new RuntimeException("Invalid message type for 'op': %s".format(t))
    }
  }

  def invite(sharer:String, user: String, opId: String){
    val session = ops(opId)
    if (!session.collaborators.contains(user) && !session.invited.contains(user)){
      session.invited += user
      notifyOne(user, JsObject(Seq(
        "service" -> JsString("op"),
        "type" -> JsString("shared"),
        "sharer" -> JsString(sharer),
        "documentId" -> JsString(session.documentId),
        "opId" -> JsString(session.opSessionId.toString)
      )))
    }
  }

  def leave(user: String, opId: String) {
    if (ops.contains(opId)) {

      val session = ops(opId)

      session.collaborators.foreach {
        collaborator =>
          notifyOne(collaborator, JsObject(Seq(
            "service" -> JsString("op"),
            "type" -> JsString("left"),
            "opId" -> JsString(opId),
            "user" -> JsString(user)
          )))
      }
      session.collaborators.remove(user)

      Logger.debug("Removed: " + user + " From Op:" + opId)
      if (session.collaborators.size == 1) {
        leave(session.collaborators.head, opId)
      } else if (session.collaborators.size == 0){
        ops.remove(opId)
        Logger.debug("Removed Op: " + opId)
      }
    }
  }

  def join(user: String, opId: String){
    val session = ops(opId)

    if (session.invited.contains(user) && !session.collaborators.contains(user)){
      notifyOne(user, JsObject(Seq(
        "service" -> JsString("op"),
        "type" -> JsString("collaborators"),
        "opId" -> JsString(opId),
        "collaborators" -> JsArray(session.collaborators.map(t => JsString(t)).toSeq)
      )))
      session.operations.foreach(
      op =>
       notifyOne(user, op)
      )

      session.collaborators += user
      session.invited -= user

      (session.collaborators.toList).foreach(
        collaborator =>
          notifyOne(collaborator, JsObject(Seq(
          "service" -> JsString("op"),
          "type" -> JsString("joined"),
          "user" -> JsString(user),
          "opId" -> JsString(opId)
        )))
      )
    }

  }

  def shareDocument(user: String, documentId: String, collaborators: List[String], fileContents: String) {

    val nextOpSessionId = (if (ops.size > 0) ops.keys.map(_.toInt).toList.max + 1 else 0).toString

    /*
     * Always resend the notification, allow the client to deal with multiple notifications
     * on their end
     */
    val userJson = (user :: collaborators).map {
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

    val session = new OpSession(nextOpSessionId.toInt, documentId)
    session.invited ++= user :: collaborators
    session.operations += JsObject(Seq(
      "service" -> JsString("op"),
      "type" -> JsString("replace"),
      "user" -> JsString(user),
      "opId" -> JsString(nextOpSessionId),
      "documentId" -> JsString(documentId),
      "content" -> JsString(fileContents),
      "start" -> JsNumber(0),
      "end" -> JsNumber(Integer.MAX_VALUE),
      "tickStamp" -> JsNumber(0)
    ))
    ops += ((nextOpSessionId, session))
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
    def handle(opSession: OpSession) : JsValue = {
      JsObject(Seq(
          "service" -> JsString("op"),
          "type" -> JsString("insert"),
          "user" -> JsString(user),
          "opId" -> JsString(opId),
          "documentId" -> JsString(opSession.documentId),
          "content" -> JsString(content),
          "start" -> JsNumber(start),
          "tickStamp" -> JsNumber(tickStamp)
        ))
    }

    replicateOp(user, opId, handle)
  }

  def replace(user: String, opId: String, content: String, start: Int, end: Int, tickStamp: Long) {
    def handle(opSession: OpSession) : JsValue = {
      JsObject(Seq(
          "service" -> JsString("op"),
          "type" -> JsString("replace"),
          "user" -> JsString(user),
          "opId" -> JsString(opId),
          "documentId" -> JsString(opSession.documentId),
          "content" -> JsString(content),
          "start" -> JsNumber(start),
          "end" -> JsNumber(end),
          "tickStamp" -> JsNumber(tickStamp)
        ))
    }

    replicateOp(user, opId, handle)
  }

  def delete(user: String, opId: String, start: Int, end: Int, tickStamp: Long) {
    def handle(opSession: OpSession) : JsValue = {
        JsObject(Seq(
          "service" -> JsString("op"),
          "type" -> JsString("delete"),
          "user" -> JsString(user),
          "opId" -> JsString(opId),
          "documentId" -> JsString(opSession.documentId),
          "start" -> JsNumber(start),
          "end" -> JsNumber(end),
          "tickStamp" -> JsNumber(tickStamp)
        ))
  }

    replicateOp(user, opId, handle)
  }


  /**
   * Perform the replication of an operational transformation event
   *
   * This sends the Op event to all respective clients
   *
   * @param user
   * User sending the event
   * @param opId
   * Operational session Id
   * @param handle
   * Handle each collaborator (push the changes)
   */
  def replicateOp(user: String, opId: String, handle: (OpSession) => JsValue) {
    if (ops.contains(opId)) {
      val opSession = ops(opId)

      val validUser =  opSession.collaborators.contains(user)

      if (validUser) {
        /*
         * Notify all clients of this operational transformational session that an insert
         * operation has been performed
         */

        opSession.operations += handle(opSession);

        (opSession.collaborators.toList).foreach {
          collaborator => {
            Logger.info(collaborator)
            notifyOne(collaborator, handle(opSession))
          }
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
class OpSession(val opSessionId: Int, val documentId: String) {

  /**
   * Collaborator user ids
   */
  val invited = new MutableHashSet[String]

  val collaborators = new MutableHashSet[String]

  val operations = new mutable.MutableList[JsValue]

  private val _start = System.currentTimeMillis()

  /**
   * Time since op session has been started
   * @return
   */
  def clock = System.currentTimeMillis() - _start
}

object OpSessions {
  /**
   * Document Id -> Shared operational transformation
   */
  val ops = new HashMap[String, OpSession]
}