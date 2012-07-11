package com.cahoot.eclipse.widget
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.SWT

trait CahootOkCancel {
  var ok: Button = null
  var cancel: Button = null
  
    protected def setupButtons(panel: Composite): Unit = {
    ok = new Button(panel, SWT.BORDER)
    ok.setText("&OK")

    cancel = new Button(panel, SWT.BORDER)
    cancel.setText("&Cancel")
  }

  protected def setupMigButtons(panel: Composite): Unit = {
	setupButtons(panel)
	ok.setLayoutData("newline, tag ok, split 2")
    cancel.setLayoutData("tag cancel")
  }
}