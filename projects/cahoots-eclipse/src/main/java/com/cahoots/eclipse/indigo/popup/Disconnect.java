package com.cahoots.eclipse.indigo.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.cahoots.eclipse.collab.ConnectDialog;

public class Disconnect implements IWorkbenchWindowActionDelegate {
	
	private Shell shell = null;
	
	@Override
	public void run(IAction arg0) {
		ConnectDialog dia = new ConnectDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dia.open();
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void init(IWorkbenchWindow window) {
		shell = window.getShell();
		
	}

}
