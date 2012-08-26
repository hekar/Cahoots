package com.cahoot.eclipse.swt

/**
 * Result from a dialog that has closed wants to return a result
 */
object CahootDialogResult extends Enumeration {
  val None, Ok, Cancel, Yes, No, Abort, Retry, Ignore = CahootDialogResult
}