package com.cahoots.eclipse.indigo.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.DisconnectDialog;
import com.google.inject.Injector;

public class Disconnect implements IWorkbenchWindowActionDelegate {

	private Shell shell;
	private CahootsConnection connection;

	@Override
	public void init(IWorkbenchWindow window) {
		shell = window.getShell();

		Injector injector = Activator.getInjector();
		connection = injector.getInstance(CahootsConnection.class);
	}

	@Override
	public void run(IAction action) {
		boolean authenticated = connection.isAuthenticated();
		if (authenticated) {
			DisconnectDialog dialog = new DisconnectDialog(shell,
					SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialog.open();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
	}
}
