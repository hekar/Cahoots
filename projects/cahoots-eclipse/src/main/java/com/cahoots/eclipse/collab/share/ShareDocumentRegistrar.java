package com.cahoots.eclipse.collab.share;

import javax.inject.Inject;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.event.EventRegistrar;
import com.cahoots.eclipse.indigo.widget.TextEditorTools;
import com.cahoots.eclipse.swt.SwtDisplayUtils;
import com.cahoots.events.OpDeleteEventListener;
import com.cahoots.events.OpInsertEventListener;
import com.cahoots.events.OpReplaceEventListener;
import com.cahoots.events.ShareDocumentEventListener;
import com.cahoots.json.receive.OpDeleteMessage;
import com.cahoots.json.receive.OpInsertMessage;
import com.cahoots.json.receive.OpReplaceMessage;
import com.cahoots.json.receive.ShareDocumentMessage;

public class ShareDocumentRegistrar implements EventRegistrar {

	private final ShareDocumentManager shareDocumentManager;
	private final CahootsSocket cahootsSocket;
	private final TextEditorTools editorTools;

	@Inject
	public ShareDocumentRegistrar(
			final ShareDocumentManager shareDocumentManager,
			final CahootsSocket cahootsSocket, final TextEditorTools editorTools) {
		this.shareDocumentManager = shareDocumentManager;
		this.cahootsSocket = cahootsSocket;
		this.editorTools = editorTools;
	}

	@Override
	public void registerEvents() {
		setupDefaultNotifications();
	}

	private void setupDefaultNotifications() {
		cahootsSocket
		.addShareDocumentEventListener(new ShareDocumentEventListener() {
			@Override
			public void onEvent(final ShareDocumentMessage msg) {
				// TODO: Handle incoming document share
			}
		});

		
		cahootsSocket.addOpInsertEventListener(new OpInsertEventListener() {
			@Override
			public void onEvent(final OpInsertMessage msg) {
				final Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							final int start = msg.getStart();
							final String contents = msg.getContent();
							final Long tickStamp = msg.getTickStamp();

							final ITextEditor textEditor = editorTools
									.getTextEditor();
							final IDocumentProvider documentProvider = textEditor
									.getDocumentProvider();
							final IDocument document = documentProvider
									.getDocument(textEditor.getEditorInput());
							document.replace(start, 0, contents);
						} catch (final BadLocationException e) {
							e.printStackTrace();
						}
					}
				};

				SwtDisplayUtils.async(runnable);
			}
		});

		cahootsSocket.addOpReplaceEventListener(new OpReplaceEventListener() {
			@Override
			public void onEvent(final OpReplaceMessage msg) {
				final Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
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

							final Long tickStamp = msg.getTickStamp();

							final ITextEditor textEditor = editorTools
									.getTextEditor();
							final IDocumentProvider documentProvider = textEditor
									.getDocumentProvider();
							final IDocument document = documentProvider
									.getDocument(textEditor.getEditorInput());
							document.replace(start, length, contents);
						} catch (final BadLocationException e) {
							e.printStackTrace();
						}
					}
				};

				SwtDisplayUtils.async(runnable);
			}
		});

		cahootsSocket.addOpDeleteEventListener(new OpDeleteEventListener() {
			@Override
			public void onEvent(final OpDeleteMessage msg) {
				final Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							final int start = msg.getStart();
							final int length = msg.getEnd() - msg.getStart();
							final Long tickStamp = msg.getTickStamp();

							final ITextEditor textEditor = editorTools
									.getTextEditor();
							final IDocumentProvider documentProvider = textEditor
									.getDocumentProvider();
							final IDocument document = documentProvider
									.getDocument(textEditor.getEditorInput());
							document.replace(start, length, "");
						} catch (final BadLocationException e) {
							e.printStackTrace();
						}
					}
				};

				SwtDisplayUtils.async(runnable);
			}
		});
	}

}
