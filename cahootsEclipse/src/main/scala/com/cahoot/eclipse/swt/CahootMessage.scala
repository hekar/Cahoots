package com.cahoot.eclipse.swt
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.MessageBox
import org.eclipse.swt.SWT

object CahootMessage {

  def error(parent: Shell, title: String, message: String): Unit = {
    val mb = new MessageBox(parent, SWT.ICON_ERROR | SWT.OK);

    mb.setText(title);
    mb.setMessage(message);
    val result = mb.open()

    result match {
      case SWT.OK =>
        CahootDialogResult.Ok
      case SWT.CANCEL =>
        CahootDialogResult.Cancel
      case SWT.RETRY =>
        CahootDialogResult.Retry
      case SWT.ABORT =>
        CahootDialogResult.Abort
      case SWT.IGNORE =>
        CahootDialogResult.Ignore
      case SWT.YES =>
        CahootDialogResult.Yes
      case SWT.NO =>
        CahootDialogResult.No
      case _ =>
        CahootDialogResult.None
    }
  }

}