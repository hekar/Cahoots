package com.cahoots.eclipse.indigo.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.DisconnectDialog;
import com.cahoots.events.ConnectEvent;
import com.cahoots.events.ConnectEventListener;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;

public class Disconnect implements IWorkbenchWindowActionDelegate, ConnectEventListener, DisconnectEventListener {
	
	private Shell shell = null;
	
	@Override
	public void run(IAction arg0) {
		if(Activator.getAuthToken() == null)
		{
			return;
		}
		DisconnectDialog dia = new DisconnectDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
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

	@Override
	public void userDisconnected(DisconnectEvent event) {
		//TODO disable button
		
	}

	@Override
	public void connected(ConnectEvent event) {
		// TODO enable button
		
	}

}
