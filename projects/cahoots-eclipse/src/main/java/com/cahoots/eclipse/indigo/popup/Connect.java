package com.cahoots.eclipse.indigo.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.events.ConnectEvent;
import com.cahoots.events.ConnectEventListener;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;

public class Connect implements IWorkbenchWindowActionDelegate {

	private CahootsSocket socket;
	private ConnectStuff stuff;

	@Override
	public void init(final IWorkbenchWindow window) {
		socket = Activator.getInjector().getInstance(CahootsSocket.class);
		stuff = Activator.getInjector().getInstance(ConnectStuff.class);

		socket.addConnectEventListener(new ConnectEventListener() {
			@Override
			public void connected(final ConnectEvent event) {
			}
		});

		socket.addDisconnectEventListener(new DisconnectEventListener() {
			@Override
			public void onEvent(final DisconnectEvent msg) {
			}
		});
	}

	@Override
	public void run(final IAction action) {
		stuff.connect();
	}

	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
	}

	@Override
	public void dispose() {
	}

}
