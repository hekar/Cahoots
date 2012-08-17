package com.cahoot.models
import java.util.Date

class ChatReceiveModel(
  val sender: PersonModel,
  val timestamp: Date,
  val message: String)
