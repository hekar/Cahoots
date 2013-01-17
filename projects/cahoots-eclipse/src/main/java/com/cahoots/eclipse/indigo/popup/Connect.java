package com.cahoots.eclipse.indigo.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.ConnectDialog;
import com.cahoots.eclipse.indigo.widget.MessageDialog;
import com.cahoots.eclipse.indigo.widget.MessageDialogStatus;
import com.cahoots.events.ConnectEvent;
import com.cahoots.events.ConnectEventListener;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;

public class Connect implements IWorkbenchWindowActionDelegate {

	private Shell shell = null;
	private CahootsConnection connection;
	private CahootsSocket socket;
	private MessageDialog messageDialog;

	@Override
	public void init(IWorkbenchWindow window) {
		connection = Activator.getInjector().getInstance(CahootsConnection.class);
		socket = Activator.getInjector().getInstance(CahootsSocket.class);
		messageDialog = Activator.getInjector()
				.getInstance(MessageDialog.class);

		socket.addConnectEventListener(new ConnectEventListener() {
			@Override
			public void connected(ConnectEvent event) {
			}
		});

		socket.addDisconnectEventListener(new DisconnectEventListener() {
			@Override
			public void userDisconnected(DisconnectEvent event) {
			}
		});

		shell = window.getShell();
	}

	@Override
	public void run(IAction action) {
		boolean connected = connection.isAuthenticated();
		
		if (connected) {
			MessageDialogStatus prompt = messageDialog.prompt(shell, "Already Connected?",
					"Would you like to disconnect before connecting to another server?");
			
			if (prompt == MessageDialogStatus.OK) {
				socket.disconnect();
			}
		}
		
		ConnectDialog dialog = new ConnectDialog(shell);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

}
