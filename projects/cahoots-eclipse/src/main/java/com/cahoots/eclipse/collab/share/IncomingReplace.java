package com.cahoots.eclipse.collab.share;

import javax.inject.Inject;

import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.text.undo.DocumentUndoManagerRegistry;
import org.eclipse.text.undo.IDocumentUndoManager;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.eclipse.op.OpMemento;
import com.cahoots.eclipse.op.OpSession;
import com.cahoots.eclipse.op.OpSessionRegister;
import com.cahoots.eclipse.swt.SwtDisplayUtils;
import com.cahoots.events.OpReplaceEventListener;
import com.cahoots.json.receive.OpReplaceMessage;

public class IncomingReplace implements OpReplaceEventListener {

	private final OpSessionRegister opSessionRegister;
	private final ShareDocumentManager shareDocumentManager;
	private final CahootsConnection cahootsConnection;
	private final ITextEditor textEditor;
	private String documentId;
	private String opId;

	@Inject
	public IncomingReplace(final OpSessionRegister opSessionRegister,
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
	public void onEvent(final OpReplaceMessage msg) {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				synchronized (opId) {
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
						final String contents = msg.getContent();
						int length = msg.getEnd() - msg.getStart();

						if (length == 0) {
							throw new IllegalStateException(
									"Length of replace message cannot be 0");
						} else if (length < 0) {
							throw new IllegalStateException(
									"Length of replace message cannot be less than 0");
						}

						final IDocumentProvider documentProvider = textEditor
								.getDocumentProvider();
						final IDocument document = documentProvider
								.getDocument(textEditor.getEditorInput());

						if (length > document.getLength()) {
							length = document.getLength();
						}

						final OpSession session = opSessionRegister
								.getSession(msg.getOpId());
						final OpMemento memento = session.getMemento();

						try {
							DocumentUndoManagerRegistry.disconnect(document);
						} catch (final Exception e) {
						}

						shareDocumentManager.disableEvents();
						if (memento.getLatestTimestamp() < msg.getTickStamp()) {
							document.replace(start, length, contents);
						} else {
							memento.addTransformation(msg);
							final String content = memento.getContent();
							document.replace(0, content.length(), content);
						}
						shareDocumentManager.enableEvents();

						DocumentUndoManagerRegistry.connect(document);

					} catch (final BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		};

		SwtDisplayUtils.async(runnable);
	}
}
