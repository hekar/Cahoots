package com.cahoots.eclipse.collab.share;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IElementStateListener;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.indigo.job.BackgroundJobScheduler;
import com.cahoots.eclipse.op.OpSessionManager;
import com.cahoots.events.ShareDocumentEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.send.SendOpInsertMessage;
import com.cahoots.json.send.SendShareDocumentMessage;

public class ShareDocumentManager {

	private final CahootsSocket cahootsSocket;
	private final OpSessionManager opSessionManager;
	private final CahootsConnection connection;
	private final BackgroundJobScheduler backgroundJobScheduler;

	/**
	 * TODO: Move into session manager
	 */
	private static AtomicLong tickStamp = new AtomicLong(0);

	@Inject
	public ShareDocumentManager(CahootsConnection connection,
			CahootsSocket cahootsSocket, OpSessionManager opSessionManager,
			BackgroundJobScheduler backgroundJobScheduler) {
		this.connection = connection;
		this.cahootsSocket = cahootsSocket;
		this.opSessionManager = opSessionManager;
		this.backgroundJobScheduler = backgroundJobScheduler;
	}

	public void shareDocument(ITextEditor textEditor,
			Collection<Collaborator> collaborators) {
		if (textEditor == null) {
			throw new NullPointerException("textEditor cannot be null");
		} else if (collaborators == null) {
			throw new NullPointerException("collaborators cannot be null");
		} else if (collaborators.size() == 0) {
			throw new IllegalArgumentException(
					"must have at least 1 collaborator");
		}

		List<String> collaboratorUsernames = extract(collaborators, on(Collaborator.class).getUsername());
		
		// TODO: Get Document ID
		final String documentId = "1";
		SendShareDocumentMessage message = new SendShareDocumentMessage(
				connection.getUsername(), documentId, collaboratorUsernames);

		final ShareDocumentMessage response = cahootsSocket
				.sendAndWaitForResponse(message, ShareDocumentMessage.class,
						ShareDocumentEventListener.class);
		
		final IDocumentProvider documentProvider = textEditor
				.getDocumentProvider();
		addElementStateListener(documentProvider);

		final IDocument document = documentProvider.getDocument(textEditor
				.getEditorInput());
		addDocumentListener(document, response.getOpId(), response.getDocumentId());
	}

	private void addElementStateListener(IDocumentProvider documentProvider) {
		IElementStateListener elementStateListener = new IElementStateListener() {

			@Override
			public void elementMoved(Object originalElement, Object movedElement) {
			}

			@Override
			public void elementDirtyStateChanged(Object element, boolean isDirty) {
			}

			@Override
			public void elementDeleted(Object element) {
				System.out.println(element);
			}

			@Override
			public void elementContentReplaced(Object element) {
				System.out.println(element);
			}

			@Override
			public void elementContentAboutToBeReplaced(Object element) {
			}
		};

		documentProvider.addElementStateListener(elementStateListener);
	}

	private void addDocumentListener(final IDocument document, final String opId, final String documentId) {
		IDocumentListener documentListener = new IDocumentListener() {

			/**
			 * Handle insert operation
			 */
			@Override
			public void documentChanged(DocumentEvent event) {
				int offset = event.fOffset;
				String username = connection.getUsername();
				String inserted = event.getText();
				long nextTickStamp = tickStamp.incrementAndGet();

				SendOpInsertMessage insert = new SendOpInsertMessage();
				insert.setOpId(opId);
				insert.setUser(username);
				insert.setContents(inserted);
				insert.setStart(offset);
				insert.setDocumentId(documentId);
				insert.setTickStamp(nextTickStamp);
				cahootsSocket.send(insert);
			}

			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
			}
		};

		document.addDocumentListener(documentListener);
	}
}
