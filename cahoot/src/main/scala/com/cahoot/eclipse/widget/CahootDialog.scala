package com.cahoot.eclipse.widget

import org.eclipse.jface.window.ApplicationWindow
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.SWT

/**
 * Standard dialog window
 */
abstract class CahootDialog extends ApplicationWindow(null)
  with GuiSetup {

  setBlockOnOpen(true)

  protected override def createContents(parent: Composite): Control = {
    dialogSetup(parent)
  }

  protected def dialogSetup(
    parent: Composite,
    preSetup: (Composite) => Unit = { _ => () },
    postSetup: (Composite) => Unit = { _ => () },
    postSetupDefaults: (Composite) => Unit = { _ => () }): Control = {

    val panel = new Composite(parent, SWT.NONE)

    preSetup(panel)
    setupContents(panel)
    postSetup(panel)
    setupDefaults(panel)
    addListeners

    panel
  }

}