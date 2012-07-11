package com.cahoot.eclipse.widget

import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control

abstract class CahootMigOkCancelDialog extends CahootDialog
  with CahootOkCancel {

  protected override def createContents(parent: Composite): Control = {
    val preSetup = { (panel: Composite) => ()
    }

    val postSetup = { (panel: Composite) =>
      setupMigButtons(panel)
    }

    dialogSetup(parent, preSetup, postSetup)
  }

}