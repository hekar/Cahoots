package com.cahoot.client.services

import com.cahoot.models.ChatSendModel
import com.cahoot.models.ChatReceiveModel
import scala.collection.mutable.ListBuffer
import com.cahoot.eclipse.core.ThreadQueueRunner
import com.cahoot.models.ChatPersonModel

class ChatService {

  private val receivers = new ListBuffer[(ChatReceiveModel) => Unit]

  /**
   * Send a chat message
   */
  def sendMessage(message: ChatSendModel): Unit = {

  }

  /**
   * Listen for incoming chat messages
   *
   * @param listener
   * 	Who is the owner of the receiver
   * @param receiver
   * 	Receiving listener
   */
  def receiveMessageAsync(listener: Any, receiver: (ChatReceiveModel) => Unit): Unit = {
    receivers += receiver
  }
  
  def listFriends(receiver: (List[ChatPersonModel]) => Unit): Unit = {
    ThreadQueueRunner.run({ () =>
      receiver.apply(List[ChatPersonModel](null))
    })
  }
}