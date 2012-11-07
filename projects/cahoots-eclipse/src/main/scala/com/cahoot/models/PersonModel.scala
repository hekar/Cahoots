package com.cahoot.models

/**
 * Basic information for a person. Please create new models and access
 * referentially for avatars, etc.
 */
class PersonModel(
  var guid: String,
  val username: String,
  val email: String)