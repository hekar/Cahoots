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
import org.eclipse.ui.IWorkbenchWindowActionDelegate
import org.eclipse.ui.IWorkbenchWindow
import org.eclipse.jface.dialogs.MessageDialog
import com.cahoot.eclipse.collab.DisconnectDialog

class Disconnect extends IWorkbenchWindowActionDelegate {

  private var shell: Shell = _

  def setActivePart(action: IAction, targetPart: IWorkbenchPart): Unit = {
    shell = targetPart.getSite().getShell();
  }

  def run(action: IAction): Unit = {
    var dialog = new DisconnectDialog()
    dialog.open()
  }

  def selectionChanged(action: IAction, selection: ISelection) = {
  }
  
  def dispose: Unit = {}
  def init(window : IWorkbenchWindow): Unit = {}
}
