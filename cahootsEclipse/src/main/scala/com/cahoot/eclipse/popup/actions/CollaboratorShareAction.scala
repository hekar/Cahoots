package com.cahoot.eclipse.popup.actions
import org.eclipse.ui.IObjectActionDelegate
import org.eclipse.swt.widgets.Shell
import org.eclipse.jface.action.IAction
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.jface.window.ApplicationWindow
import org.eclipse.jface.viewers.ISelection
import com.cahoot.eclipse.collab.CollaboratorSelectionDialog

class CollaboratorShareAction extends IObjectActionDelegate {

  private var shell: Shell = _

  def setActivePart(action: IAction, targetPart: IWorkbenchPart): Unit = {
    shell = targetPart.getSite().getShell();
  }

  def run(action: IAction): Unit = {
    val dialog = new CollaboratorSelectionDialog();
    dialog.open();
  }

  /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  def selectionChanged(action: IAction, selection: ISelection) = {
  }

}