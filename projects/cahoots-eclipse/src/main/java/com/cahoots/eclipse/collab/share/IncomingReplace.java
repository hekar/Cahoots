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
import com.cahoots.events.OpReplaceEventListener;
import com.cahoots.json.receive.OpReplaceMessage;

public class IncomingReplace implements OpReplaceEventListener {

	private final OpSessionRegister opSessionRegister;
	private final ShareDocumentManager shareDocumentManager;
	private final CahootsConnection cahootsConnection;
	private final ITextEditor textEditor;

	@Inject
	public IncomingReplace(final OpSessionRegister opSessionRegister,
			final ShareDocumentManager shareDocumentManager,
			final CahootsConnection cahootsConnection,
			final ITextEditor textEditor) {
		this.opSessionRegister = opSessionRegister;
		this.cahootsConnection = cahootsConnection;
		this.shareDocumentManager = shareDocumentManager;
		this.textEditor = textEditor;
	}

	@Override
	public void onEvent(final OpReplaceMessage msg) {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					if (msg.getUser().equals(cahootsConnection.getUsername())) {
						return;
					}

					final int start = msg.getStart();
					final String contents = msg.getContent();
					final int length = msg.getEnd() - msg.getStart();

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

					// TODO: Do not apply
					shareDocumentManager.disableEvents();
					document.replace(start, length, contents);
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
