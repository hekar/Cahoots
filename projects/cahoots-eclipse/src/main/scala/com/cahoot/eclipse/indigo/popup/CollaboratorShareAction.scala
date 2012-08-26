package com.cahoot.eclipse.indigo.popup
import org.eclipse.ui.IObjectActionDelegate
import org.eclipse.swt.widgets.Shell
import org.eclipse.jface.action.IAction
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.jface.window.ApplicationWindow
import org.eclipse.jface.viewers.ISelection
import com.cahoot.eclipse.collab.CollaboratorSelectionDialog
import org.eclipse.ui.IEditorActionDelegate
import org.eclipse.ui.IEditorPart

class CollaboratorShareAction extends IObjectActionDelegate with IEditorActionDelegate {

  private var shell: Shell = _

  def setActivePart(action: IAction, targetPart: IWorkbenchPart): Unit = {
    shell = targetPart.getSite().getShell();
  }

  def run(action: IAction): Unit = {
    val dialog = new CollaboratorSelectionDialog();
    dialog.open();
  }

  def selectionChanged(action: IAction, selection: ISelection) = {
  }

  def setActiveEditor(action: IAction, targetEditor: IEditorPart): Unit = {

  };
}
