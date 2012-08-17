package com.cahoot.models

/**
 * Send a chat Message to the Cahoot server
 */
class ChatSendModel(
  val sender: PersonModel,
  val recipients: List[PersonModel],
  val message: String)