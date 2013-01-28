package com.cahoots.eclipse.indigo.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.ConnectDialog;
import com.cahoots.eclipse.collab.share.ShareDocumentDialog;
import com.google.inject.Injector;

public class ShareDocument implements IObjectActionDelegate,
		IEditorActionDelegate {

	private Shell shell;
	private CahootsSocket cahootsSocket;
	
	@SuppressWarnings("unused")
	private IWorkbenchPart targetPart;
	
	@SuppressWarnings("unused")
	private IEditorPart targetEditor;

	public ShareDocument() {
		final Injector injector = Activator.getInjector();
		cahootsSocket = injector.getInstance(CahootsSocket.class);
	}

	@Override
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		this.targetPart = targetPart;
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		shell = targetEditor.getSite().getShell();
		this.targetEditor = targetEditor;
	}

	@Override
	public void run(final IAction action) {
		if (!cahootsSocket.isConnected()) {
			final ConnectDialog connectDialog = new ConnectDialog(shell);
			connectDialog.setBlockOnOpen(true);
			connectDialog.open();
			
			if (connectDialog.getReturnCode() == SWT.CANCEL) {
				return;
			}
		}

		// TODO: Cahoots connection dialog, should double check if connected?
		final ShareDocumentDialog dialog = new ShareDocumentDialog(shell);
		dialog.open();
	}

	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		
		if (selection instanceof TextSelection) {
			// TODO: Handle text selection and cursor movement changes here
			@SuppressWarnings("unused")
			final
			TextSelection textSelection = (TextSelection) selection;
		}
	}
}
