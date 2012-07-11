package com.cahoot.eclipse.collab

import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.window.ApplicationWindow
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Table
import org.eclipse.swt.SWT
import com.cahoot.eclipse.widget.listener.SwtListeners
import net.miginfocom.swt.MigLayout
import com.cahoot.eclipse.core.PluginConstants
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.jface.viewers.ColumnLabelProvider
import com.cahoot.eclipse.widget.CahootWindow
import com.cahoot.eclipse.widget.CahootOkCancelDialog
import com.cahoot.eclipse.widget.CahootMigOkCancelDialog

class ConnectionSelectionDialog extends CahootMigOkCancelDialog {

  var help: Label = _
  var view: TableViewer = _
  var table: Table = _
  var manage: Button = _

  override def setupContents(panel: Composite): Unit = {
    panel.setLayout(new MigLayout("fill", "", "[growprio 0][growprio 0][growprio 100][growprio 0]"))

    help = new Label(panel, SWT.NONE)
    help.setText("Help text, blah, blah...")
    help.setLayoutData("")

    manage = new Button(panel, SWT.NONE)
    manage.setText("&Manage Connections")
    manage.setLayoutData("newline")

    view = new TableViewer(panel, SWT.BORDER)
    table = view.getTable()
    table.setLayoutData("newline, grow")

    view.setContentProvider(new CollaboratorSelectionContentProvider)
    view.setLabelProvider(new CollaboratorSelectionLabelProvider)

    view.setInput(new Object())

    if (table.getItemCount() > 0) {
      table.setSelection(0)
    }

    table.setFocus()

    val col = new TableViewerColumn(view, SWT.NONE);
    col.getColumn().setWidth(200);
    col.getColumn().setText("Firstname:");
    col.setLabelProvider(new ColumnLabelProvider() {
      override def getText(element: Object): String = {
        element.toString()
      }
    });

  }

  override def setupDefaults(panel: Composite): Unit = {
    val shell = getShell()
    shell.setText("%s Select Connection".format(PluginConstants.APP_TITLE))
    shell.setSize(800, 600)
  }

  override def addListeners(): Unit = {
    manage.addSelectionListener(SwtListeners.selectionListener({ e =>

    }))

    table.addListener(SWT.Selection, SwtListeners.listener({ e =>
      ok.setEnabled(table.getSelectionCount() > 0)
    }))

    ok.addSelectionListener(SwtListeners.selectionListener({ e =>
      close
    }))

    cancel.addSelectionListener(SwtListeners.selectionListener({ e =>
      close
    }))

  }

}