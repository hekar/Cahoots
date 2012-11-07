package com.cahoot.eclipse.indigo.popup

import org.eclipse.jface.action.IAction
import org.eclipse.jface.viewers.ISelection
import org.eclipse.swt.widgets.Shell
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.IWorkbenchWindow
import org.eclipse.ui.IWorkbenchWindowActionDelegate

import com.cahoot.eclipse.collab.ConnectDialog

class Connect extends IWorkbenchWindowActionDelegate {

  private var shell: Shell = _

  def setActivePart(action: IAction, targetPart: IWorkbenchPart): Unit = {
    shell = targetPart.getSite().getShell();
  }

  def run(action: IAction): Unit = {
    var dialog = new ConnectDialog()
    dialog.open()
  }

  def selectionChanged(action: IAction, selection: ISelection) = {
  }
  
  def dispose: Unit = {}
  def init(window : IWorkbenchWindow): Unit = {}
}
