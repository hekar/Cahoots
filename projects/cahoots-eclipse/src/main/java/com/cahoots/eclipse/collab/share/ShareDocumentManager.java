package com.cahoots.eclipse.collab.share;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.indigo.job.BackgroundJobScheduler;
import com.cahoots.eclipse.op.OpSessionManager;
import com.cahoots.events.ShareDocumentEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.send.SendOpDeleteMessage;
import com.cahoots.json.send.SendOpInsertMessage;
import com.cahoots.json.send.SendOpReplaceMessage;
import com.cahoots.json.send.SendShareDocumentMessage;

public class ShareDocumentManager {

	private boolean enabled = true;
	private final CahootsSocket cahootsSocket;
	private final OpSessionManager opSessionManager;
	private final CahootsConnection connection;
	private final BackgroundJobScheduler backgroundJobScheduler;

	/**
	 * TODO: Move into session manager
	 */
	private static AtomicLong tickStamp = new AtomicLong(0);

	@Inject
	public ShareDocumentManager(final CahootsConnection connection,
			final CahootsSocket cahootsSocket,
			final OpSessionManager opSessionManager,
			final BackgroundJobScheduler backgroundJobScheduler) {
		this.connection = connection;
		this.cahootsSocket = cahootsSocket;
		this.opSessionManager = opSessionManager;
		this.backgroundJobScheduler = backgroundJobScheduler;
	}

	public void shareDocument(final ITextEditor textEditor,
			final Collection<Collaborator> collaborators) {
		if (textEditor == null) {
			throw new NullPointerException("textEditor cannot be null");
		} else if (collaborators == null) {
			throw new NullPointerException("collaborators cannot be null");
		} else if (collaborators.size() == 0) {
			throw new IllegalArgumentException(
					"must have at least 1 collaborator");
		}

		final List<String> collaboratorUsernames = extract(collaborators,
				on(Collaborator.class).getUsername());

		final IEditorInput editorInput = textEditor.getEditorInput();
		if (editorInput == null) {
			throw new IllegalStateException("Text editor has no editorInput");
		}

		// Get resource for text editor
		final IResource resource = ResourceUtil.getResource(editorInput);
		final String documentId = resource.getFullPath().toString();

		final SendShareDocumentMessage message = new SendShareDocumentMessage(
				connection.getUsername(), documentId, collaboratorUsernames);

		final ShareDocumentMessage response = cahootsSocket
				.sendAndWaitForResponse(message, ShareDocumentMessage.class,
						ShareDocumentEventListener.class);

		final String opId = response.getOpId();

		final IDocumentProvider documentProvider = textEditor
				.getDocumentProvider();

		if (documentProvider == null) {
			throw new IllegalStateException(
					"Document provider must not be null");
		}

		final IDocument document = documentProvider.getDocument(editorInput);
		addDocumentListener(document, opId, documentId);
	}

	public void addDocumentListener(final IDocument document,
			final String opId, final String documentId) {
		final IDocumentListener documentListener = new IDocumentListener() {

			/**
			 * Handle insert operation
			 */
			@Override
			public void documentChanged(final DocumentEvent event) {
				if (enabled) {
					final int length = event.fLength;
					final int start = event.fOffset;
					final int end = start + length;
					final String text = event.getText();
					if (length > 0 && text.isEmpty()) {
						delete(opId, documentId, start, end);
					} else if (length > 0 && !text.isEmpty()) {
						replace(opId, documentId, start, end, text);
					} else if (length == 0 && !text.isEmpty()) {
						insert(opId, documentId, start, text);
					} else {
						// ???
					}
				}
			}

			@Override
			public void documentAboutToBeChanged(final DocumentEvent event) {
			}
		};

		document.addDocumentListener(documentListener);
	}

	private void insert(final String opId, final String documentId,
			final int start, final String content) {
		final String username = connection.getUsername();
		final long nextTickStamp = tickStamp.incrementAndGet();

		final SendOpInsertMessage insert = new SendOpInsertMessage();
		insert.setOpId(opId);
		insert.setUser(username);
		insert.setContent(content);
		insert.setStart(start);
		insert.setDocumentId(documentId);
		insert.setTickStamp(nextTickStamp);
		cahootsSocket.send(insert);
	}

	private void replace(final String opId, final String documentId,
			final int start, final int end, final String content) {
		final String username = connection.getUsername();
		final long nextTickStamp = tickStamp.incrementAndGet();

		final SendOpReplaceMessage message = new SendOpReplaceMessage();
		message.setOpId(opId);
		message.setDocumentId(documentId);
		message.setStart(start);
		message.setEnd(end);
		message.setUser(username);
		message.setTickStamp(nextTickStamp);
		message.setContent(content);
		cahootsSocket.send(message);
	}

	private void delete(final String opId, final String documentId,
			final int start, final int end) {
		final String username = connection.getUsername();
		final long nextTickStamp = tickStamp.incrementAndGet();

		final SendOpDeleteMessage message = new SendOpDeleteMessage();
		message.setOpId(opId);
		message.setDocumentId(documentId);
		message.setStart(start);
		message.setEnd(end);
		message.setUser(username);
		message.setTickStamp(nextTickStamp);
		cahootsSocket.send(message);
	}

	public void enableEvents() {
		enabled = true;
	}

	public void disableEvents() {
		enabled = false;
	}
}
