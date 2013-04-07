package com.cahoots.eclipse.collab.share;

import javax.inject.Inject;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.undo.DocumentUndoManagerRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.eclipse.op.OpMemento;
import com.cahoots.eclipse.op.OpSession;
import com.cahoots.eclipse.op.OpSessionRegister;
import com.cahoots.eclipse.swt.SwtDisplayUtils;
import com.cahoots.events.OpDeleteEventListener;
import com.cahoots.json.receive.OpDeleteMessage;

public class IncomingDelete implements OpDeleteEventListener {

	private final OpSessionRegister opSessionRegister;
	private final ShareDocumentManager shareDocumentManager;
	private final CahootsConnection cahootsConnection;
	private final ITextEditor textEditor;
	private String documentId;
	private String opId;

	@Inject
	public IncomingDelete(final OpSessionRegister opSessionRegister,
			final ShareDocumentManager shareDocumentManager,
			final CahootsConnection cahootsConnection,
			final ITextEditor textEditor, final String documentId,
			final String opId) {
		this.opSessionRegister = opSessionRegister;
		this.cahootsConnection = cahootsConnection;
		this.shareDocumentManager = shareDocumentManager;
		this.textEditor = textEditor;
		this.documentId = documentId;
		this.opId = opId;
	}

	@Override
	public void onEvent(final OpDeleteMessage msg) {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
					try {
						if (!msg.getOpId().equals(opId)
								|| !msg.getDocumentId().equals(documentId)) {
							return;
						}
						if (msg.getUser().equals(
								cahootsConnection.getUsername())) {
							return;
						}

						final int start = msg.getStart();
						final int length = msg.getEnd() - msg.getStart();

						final IDocumentProvider documentProvider = textEditor
								.getDocumentProvider();
						final IDocument document = documentProvider
								.getDocument(textEditor.getEditorInput());

						final OpSession session = opSessionRegister
								.getSession(msg.getOpId());
						final OpMemento memento = session.getMemento();

						try {
							DocumentUndoManagerRegistry.disconnect(document);
						} catch (final Exception e) {
						}

						shareDocumentManager.disableEvents();
						if (memento.getLatestTimestamp() < msg.getTickStamp()) {
							document.replace(start, length, "");
						} else {
							memento.addTransformation(msg);
							final String content = memento.getContent();
							document.replace(0, document.getLength(), content);
						}
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
