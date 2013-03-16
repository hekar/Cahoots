package com.cahoots.eclipse.collab.share;

import javax.inject.Inject;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.eclipse.op.OpSession;
import com.cahoots.eclipse.op.OpSessionRegister;
import com.cahoots.eclipse.swt.SwtDisplayUtils;
import com.cahoots.events.OpInsertEventListener;
import com.cahoots.json.receive.OpInsertMessage;

public class IncomingInsert implements OpInsertEventListener {

	private final OpSessionRegister opSessionRegister;
	private final ShareDocumentManager shareDocumentManager;
	private final CahootsConnection cahootsConnection;
	private final ITextEditor textEditor;
	private String opId;
	private String documentId;

	@Inject
	public IncomingInsert(final OpSessionRegister opSessionRegister,
			final ShareDocumentManager shareDocumentManager,
			final CahootsConnection cahootsConnection,
			final ITextEditor textEditor, final String documentId,
			final String opId) {
		this.opSessionRegister = opSessionRegister;
		this.cahootsConnection = cahootsConnection;
		this.shareDocumentManager = shareDocumentManager;
		this.textEditor = textEditor;
		this.opId = opId;
		this.documentId = documentId;
	}

	@Override
	public void onEvent(final OpInsertMessage msg) {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					if (!msg.getOpId().equals(opId)
							|| !msg.getDocumentId().equals(documentId)) {
						return;
					}
					if (msg.getUser().equals(cahootsConnection.getUsername())) {
						return;
					}

					final int start = msg.getStart();
					final String contents = msg.getContent();

					final IDocumentProvider documentProvider = textEditor
							.getDocumentProvider();
					final IDocument document = documentProvider
							.getDocument(textEditor.getEditorInput());

					// TODO: Do not apply
					shareDocumentManager.disableEvents();
					document.replace(start, 0, contents);
					shareDocumentManager.enableEvents();

					final OpSession session = opSessionRegister.getSession(msg
							.getOpId());
					session.getMemento().addTransformation(msg);

				} catch (final BadLocationException e) {
					e.printStackTrace();
				}
			}
		};

		SwtDisplayUtils.async(runnable);
	}
}
