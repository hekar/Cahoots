package com.cahoot.eclipse.collab
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.window.ApplicationWindow
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.Table
import org.eclipse.swt.SWT
import net.miginfocom.swt.MigLayout
import com.cahoot.models.PersonModel
import java.util.ArrayList
import scala.collection.mutable.ListBuffer
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.jface.viewers.ColumnLabelProvider
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.events.SelectionEvent
import com.cahoot.eclipse.widget.listener.SwtListeners
import com.cahoot.eclipse.core.PluginConstants
import com.cahoot.eclipse.widget.CahootOkCancelDialog
import com.cahoot.eclipse.widget.CahootMigOkCancelDialog

/**
 * Modal dialog for selecting which collaborators the user wishes to share with
 */
class CollaboratorSelectionDialog() extends CahootMigOkCancelDialog {

  var help: Label = _
  var view: TableViewer = _
  var table: Table = _
  var manage: Button = _

  val collaborators = new ListBuffer[PersonModel]

  override def setupContents(panel: Composite): Unit = {

    panel.setLayout(new MigLayout("fill", "",
      "[growprio 0][growprio 0][growprio 100][growprio 0]"))

    help = new Label(panel, SWT.NONE)
    help.setText("Help text, blah, blah...")
    help.setLayoutData("")

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
    shell.setText("%s Collaborator".format(PluginConstants.APP_TITLE))
    shell.setSize(800, 600)
  }

  override def addListeners(): Unit = {
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

  def getCollaborators(): List[PersonModel] = collaborators.toList
  

}