package com.cahoots.eclipse.indigo.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.ConnectDialog;
import com.cahoots.eclipse.collab.ShareDocumentDialog;
import com.google.inject.Injector;

public class ShareDocument implements IObjectActionDelegate,
		IEditorActionDelegate {

	private Shell shell;
	private CahootsSocket cahootsSocket;
	private IWorkbenchPart targetPart;
	private IEditorPart targetEditor;

	public ShareDocument() {
		Injector injector = Activator.getInjector();
		cahootsSocket = injector.getInstance(CahootsSocket.class);
	}

	@Override
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		this.targetPart = targetPart;
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		shell = targetEditor.getSite().getShell();
		this.targetEditor = targetEditor;
	}

	@Override
	public void run(final IAction action) {
		if (!cahootsSocket.isConnected()) {
			ConnectDialog connectDialog = new ConnectDialog(shell);
			connectDialog.open();
		}

		final ShareDocumentDialog dialog = new ShareDocumentDialog(shell);
		dialog.open();
	}

	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		System.out.println(selection);
	}
}
