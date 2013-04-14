package com.cahoots.eclipse.collab.share;

import javax.inject.Inject;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.undo.DocumentUndoManagerRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.serialize.receive.OpDeleteMessage;
import com.cahoots.eclipse.indigo.misc.SwtDisplayUtils;
import com.cahoots.eclipse.optransformation.OpMemento;
import com.cahoots.eclipse.optransformation.OpSession;
import com.cahoots.eclipse.optransformation.OpSessionRegister;
import com.cahoots.event.OpDeleteEventListener;

public class IncomingDelete implements OpDeleteEventListener {

	private final OpSessionRegister opSessionRegister;
	private final ShareDocumentManager shareDocumentManager;
	private final ConnectionDetails ConnectionDetails;
	private final ITextEditor textEditor;
	private String documentId;
	private String opId;

	@Inject
	public IncomingDelete(final OpSessionRegister opSessionRegister,
			final ShareDocumentManager shareDocumentManager,
			final ConnectionDetails ConnectionDetails,
			final ITextEditor textEditor, final String documentId,
			final String opId) {
		this.opSessionRegister = opSessionRegister;
		this.ConnectionDetails = ConnectionDetails;
		this.shareDocumentManager = shareDocumentManager;
		this.textEditor = textEditor;
		this.documentId = documentId;
		this.opId = opId;
	}

	@Override
	public synchronized void onEvent(final OpDeleteMessage msg) {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					if (!msg.getOpId().equals(opId)
							|| !msg.getDocumentId().equals(documentId)) {
						return;
					}
					if (msg.getUser().equals(ConnectionDetails.getUsername())) {
						return;
					}

					final IDocumentProvider documentProvider = textEditor
							.getDocumentProvider();
					final IDocument document = documentProvider
							.getDocument(textEditor.getEditorInput());

					msg.setStart(Math.min(msg.getStart(), document.getLength()));
					msg.setEnd(Math.min(msg.getEnd(), document.getLength()));

					final OpSession session = opSessionRegister.getSession(msg
							.getOpId());
					final OpMemento memento = session.getMemento();

					try {
						DocumentUndoManagerRegistry.disconnect(document);
					} catch (final Exception e) {
					}

					shareDocumentManager.disableEvents();
					memento.addTransformation(msg);
					final String content = memento.getContent();
					document.replace(0, document.getLength(), content);
					shareDocumentManager.enableEvents();
					DocumentUndoManagerRegistry.connect(document);
				} catch (final BadLocationException e) {
					e.printStackTrace();
				}
			}
		};

		SwtDisplayUtils.sync(runnable);
	}
}
