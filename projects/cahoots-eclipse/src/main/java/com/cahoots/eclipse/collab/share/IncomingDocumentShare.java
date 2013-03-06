package com.cahoots.eclipse.collab.share;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.indigo.editor.ResourceFinder;
import com.cahoots.eclipse.indigo.widget.MessageDialog;
import com.cahoots.eclipse.indigo.widget.MessageDialogStatus;
import com.cahoots.eclipse.indigo.widget.TextEditorTools;
import com.cahoots.eclipse.indigo.widget.UserListViewContentProvider;
import com.cahoots.eclipse.op.OpDocument;
import com.cahoots.eclipse.op.OpSessionRegister;
import com.cahoots.eclipse.swt.SwtDisplayUtils;
import com.cahoots.events.ShareDocumentEventListener;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.send.JoinCollaborationMessage;

public final class IncomingDocumentShare implements ShareDocumentEventListener {

	private final ShareDocumentManager shareDocumentManager;
	private final CahootsSocket cahootsSocket;
	private final TextEditorTools editorTools;
	private final ResourceFinder resourceFinder;
	private final IWorkbenchWindow workbenchWindow;
	private final IEditorRegistry editorRegistry;
	private final CahootsConnection cahootsConnection;
	private final MessageDialog messageDialog;
	private final UserListViewContentProvider userList;

	@Inject
	public IncomingDocumentShare(final OpSessionRegister opSessionRegister,
			final MessageDialog messageDialog,
			final CahootsConnection cahootsConnection,
			final IWorkbenchWindow workbenchWindow,
			final IEditorRegistry editorRegistry,
			final ResourceFinder resourceFinder,
			final ShareDocumentManager shareDocumentManager,
			final CahootsSocket cahootsSocket,
			final TextEditorTools editorTools,
			final UserListViewContentProvider userList) {
		this.messageDialog = messageDialog;
		this.cahootsConnection = cahootsConnection;
		this.workbenchWindow = workbenchWindow;
		this.editorRegistry = editorRegistry;
		this.resourceFinder = resourceFinder;
		this.shareDocumentManager = shareDocumentManager;
		this.cahootsSocket = cahootsSocket;
		this.editorTools = editorTools;
		this.userList = userList;
	}

	@Override
	public void onEvent(final ShareDocumentMessage msg) {
		try {
			final String documentId = msg.getDocumentId();
			final String sharer = msg.getSharer();
			final String opId = msg.getOpId();
			final String name = userList.get(sharer).getName();

			final Runnable runnable = new Runnable() {
				@Override
				public void run() {
					final OpDocument document = new OpDocument(opId, documentId);

					if (!cahootsConnection.isLoggedInUser(sharer)) {
						final String inviteMessage = String
								.format("%s is requesting to share document %s. File contents will be overwritten.",
										name, document.getFilename());
						final MessageDialogStatus prompt = messageDialog
								.prompt(workbenchWindow.getShell(),
										"Accept Invite", inviteMessage);
						if (prompt != MessageDialogStatus.OK) {
							return;
						}
					}
					cahootsSocket.send(new JoinCollaborationMessage(
							cahootsConnection.getUsername(), opId));
					final ITextEditor textEditor = getSharedDocumentTextEditor(documentId);

					final IDocument doc = textEditor.getDocumentProvider()
							.getDocument(textEditor.getEditorInput());
					shareDocumentManager.addDocumentListener(doc, opId,
							documentId, textEditor);
				}
			};

			SwtDisplayUtils.sync(runnable);
		} catch (final Exception e) {
			e.printStackTrace();
			ShareDocumentRegistrar.logger.error(e);
		}
	}

	private ITextEditor getSharedDocumentTextEditor(final String documentId) {
		final IFile file = resourceFinder.findFileByDocumentId(documentId);
		final IEditorPart editorPart = resourceFinder.findEditorByFile(file);

		final boolean editorIsAlreadyOpen = (editorPart != null);
		if (!editorIsAlreadyOpen) {
			final IEditorDescriptor defaultEditor = editorRegistry
					.getDefaultEditor(file.getFullPath().toString());
			final IEditorInput editorInput = new FileEditorInput(file);

			// Open new editor
			final IWorkbenchPage activePage = workbenchWindow.getActivePage();
			final IEditorPart editor = editorTools.openEditor(activePage,
					editorInput, defaultEditor.getId());
			if (editor == null) {
				throw new IllegalStateException("Failure to open editor for "
						+ documentId);
			}

			final ITextEditor textEditor = editorTools
					.editorPartToTextEditor(editor);
			return textEditor;
		} else {
			final IEditorInput editorInput = editorPart.getEditorInput();
			if (editorInput == null) {
				throw new IllegalStateException(
						"Failure to get editor input for documentId: "
								+ documentId);
			}

			// Open existing editor
			final IWorkbenchPage activePage = workbenchWindow.getActivePage();

			final String editorId = editorPart.getSite().getId();
			final IEditorPart editor = editorTools.openEditor(activePage,
					editorInput, editorId);
			if (editor == null) {
				throw new IllegalStateException("Failure to find editor for "
						+ documentId);
			}

			final ITextEditor textEditor = editorTools
					.editorPartToTextEditor(editor);
			return textEditor;
		}
	}

}