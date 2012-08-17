package com.cahoot.eclipse.collab

import org.eclipse.jface.viewers.TableViewer
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Table
import org.eclipse.swt.SWT

import com.cahoot.eclipse.widget.listener.SwtListeners
import com.cahoot.eclipse.widget.CahootMigOkCancelDialog

class CollaboratorModificationDialog extends CahootMigOkCancelDialog {

  var view: TableViewer = null
  var table: Table = null

  def setupContents(panel: Composite): Unit = {
    view = new TableViewer(panel, SWT.BORDER | SWT.MULTI)
    table = view.getTable()

    view.setLabelProvider(new CollaboratorModificationLabelProvider)
    view.setContentProvider(new CollaboratorModificationContentProvider)

    view.setInput(new Object())
  }

  def setupDefaults(panel: Composite): Unit = {}

  def addListeners(): Unit = {
    ok.addSelectionListener(SwtListeners.selectionListener({ e =>
      close
    }))

    cancel.addSelectionListener(SwtListeners.selectionListener({ e =>
      close
    }))
  }

}