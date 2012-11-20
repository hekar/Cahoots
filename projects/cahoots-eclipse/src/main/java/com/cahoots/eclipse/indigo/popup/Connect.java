package com.cahoots.eclipse.indigo.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.ConnectDialog;
import com.cahoots.events.ConnectEvent;
import com.cahoots.events.ConnectEventListener;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;
import com.cahoots.websocket.CahootsSocket;

public class Connect implements IWorkbenchWindowActionDelegate, ConnectEventListener, DisconnectEventListener {
	
	private Shell shell = null;
	
	@Override
	public void run(IAction arg0) {
		if(Activator.getAuthToken() == null)
		{
			ConnectDialog dia = new ConnectDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dia.open();
		}
		
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
		CahootsSocket.getInstance().addDisconnectEventListener(this);
		CahootsSocket.getInstance().addConnectEventListener(this);
		shell = window.getShell();
	}

	@Override
	public void disconnected(DisconnectEvent event) {
		//TODO enable button
	}

	@Override
	public void connected(ConnectEvent event) {
		//TODO disable button
	}

}
