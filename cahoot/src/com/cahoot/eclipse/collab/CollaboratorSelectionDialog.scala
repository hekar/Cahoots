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

/**
 * Modal dialog for selecting which collaborators the user wishes to share with
 */
class CollaboratorSelectionDialog(shell: Shell) extends ApplicationWindow(shell) {

  var help: Label = null
  var view: TableViewer = null
  var table: Table = null
  var ok : Button = null
  var cancel : Button = null

  val collaborators = new ListBuffer[PersonModel]

  setBlockOnOpen(true)

  protected override def createContents(parent: Composite): Control = {

    val panel = new Composite(parent, SWT.NONE)
    panel.setLayout(new MigLayout("fill", "", "[growprio 0][growprio 100][growprio 0]"))

    help = new Label(panel, SWT.NONE)
    help.setText("Help text, blah, blah...")
    help.setLayoutData("")

    view = new TableViewer(panel, SWT.BORDER)
    table = view.getTable()
    table.setLayoutData("newline, grow")

    view.setContentProvider(new CollaboratorSelectionContentProvider)
    view.setLabelProvider(new CollaboratorSelectionLabelProvider)

    view.setInput(new Object())
    
    val col = new TableViewerColumn(view, SWT.NONE);
    col.getColumn().setWidth(200);
    col.getColumn().setText("Firstname:");
    col.setLabelProvider(new ColumnLabelProvider() {
      override def getText(element: Object): String = {
        element.toString()
      }
    });
    
    ok = new Button(panel, SWT.BORDER)
    ok.setText("&OK")
    ok.setLayoutData("newline, split 2, tag ok")
    
    cancel = new Button(panel, SWT.BORDER)
    cancel.setText("&Cancel")
    cancel.setLayoutData("tag cancel")

    addListeners
    
    panel
  }
  
  def addListeners(): Unit = {
    
  }

  def getCollaborators(): List[PersonModel] = {
    collaborators.toList
  }
}