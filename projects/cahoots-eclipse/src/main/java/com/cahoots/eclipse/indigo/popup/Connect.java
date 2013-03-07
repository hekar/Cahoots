package com.cahoots.eclipse.indigo.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.cahoots.eclipse.Activator;

public class Connect implements IWorkbenchWindowActionDelegate {

	private ConnectStuff stuff;

	@Override
	public void init(final IWorkbenchWindow window) {
		stuff = Activator.getInjector().getInstance(ConnectStuff.class);
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
