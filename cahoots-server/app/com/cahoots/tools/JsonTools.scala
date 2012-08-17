package com.cahoots.tools
import play.api.libs.json.JsValue

/**
 * Miscellaneous functions for dealing with Json
 */
object JsonTools {
  /**
   * Clean the Json type and make it ready for consumption
   */
  def clean(s: JsValue) = s.toString().replaceAll('"'.toString(), "")
}