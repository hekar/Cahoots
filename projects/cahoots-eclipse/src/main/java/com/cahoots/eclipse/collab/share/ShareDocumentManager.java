package com.cahoots.eclipse.collab.share;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.http.tools.CahootsHttpClient;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.op.OpDocument;
import com.cahoots.eclipse.op.OpMemento;
import com.cahoots.eclipse.op.OpSession;
import com.cahoots.eclipse.op.OpSessionRegister;
import com.cahoots.eclipse.op.OpSynchronizedClock;
import com.cahoots.json.Collaborator;
import com.cahoots.json.send.InviteUserMessage;
import com.cahoots.json.send.SendOpDeleteMessage;
import com.cahoots.json.send.SendOpInsertMessage;
import com.cahoots.json.send.SendOpReplaceMessage;
import com.cahoots.json.send.SendShareDocumentMessage;

public class ShareDocumentManager {

	private boolean enabled = true;

	private final CahootsSocket cahootsSocket;
	private final OpSessionRegister opSessionRegistrar;
	private final CahootsConnection connection;
	private final CahootsHttpClient cahootsHttpClient;
	private final Map<String, ShareDocumentManager.Share> shares = new HashMap<String, ShareDocumentManager.Share>();
	private final Map<String, String> documentIds = new HashMap<String, String>();

	@Inject
	public ShareDocumentManager(final CahootsConnection connection,
			final CahootsHttpClient cahootsHttpClient,
			final CahootsSocket cahootsSocket,
			final OpSessionRegister opSessionManager) {
		this.connection = connection;
		this.cahootsHttpClient = cahootsHttpClient;
		this.cahootsSocket = cahootsSocket;
		this.opSessionRegistrar = opSessionManager;
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
		final IDocument document = textEditor.getDocumentProvider()
				.getDocument(editorInput);
		final String contents = document.get();

		final SendShareDocumentMessage message = new SendShareDocumentMessage(
				connection.getUsername(), documentId, collaboratorUsernames,
				contents);

		cahootsSocket.send(message);
	}

	public void removeDocumentListner(final String opId) {
		opSessionRegistrar.removeSession(opId);
		documentIds.remove(opId);
		if (shares.containsKey(opId)) {
			final Share s = shares.get(opId);
			cahootsSocket.removeOpDeleteEventListener(s.getIncomingDelete());
			cahootsSocket.removeOpInsertEventListener(s.getIncomingInsert());
			cahootsSocket.removeOpReplaceEventListener(s.getIncomingReplace());
			s.document.removeDocumentListener(s.documentListener);
			shares.remove(opId);
			opSessionRegistrar.removeSession(opId);
		}
	}

	public void addDocumentListener(final IDocument document,
			final String opId, final String documentId,
			final ITextEditor textEditor) {
		try {

			final IncomingInsert insert = new IncomingInsert(opSessionRegistrar,
					this, connection, textEditor, documentId, opId);
			final IncomingReplace replace = new IncomingReplace(
					opSessionRegistrar, this, connection, textEditor, documentId,
					opId);
			final IncomingDelete delete = new IncomingDelete(opSessionRegistrar,
					this, connection, textEditor, documentId, opId);

			cahootsSocket.addOpInsertEventListener(insert);
			cahootsSocket.addOpReplaceEventListener(replace);
			cahootsSocket.addOpDeleteEventListener(delete);

			documentIds.put(opId, documentId);
			final OpSynchronizedClock clock = OpSynchronizedClock
					.fromConnection(cahootsHttpClient, connection, opId).get();
			final OpDocument opDocument = new OpDocument(opId, documentId);
			final OpMemento opMemento = new OpMemento(opDocument);
			final OpSession opSession = new OpSession(opMemento, clock);
			opSessionRegistrar.addSession(opId, opSession);

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
			shares.put(opId, new Share(document, documentListener, delete,
					insert, replace));
			document.addDocumentListener(documentListener);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final ExecutionException e) {
			e.printStackTrace();
		}
	}

	public String getDocumentId(final String opId) {
		return documentIds.get(opId);
	}

	private void insert(final String opId, final String documentId,
			final int start, final String content) {
		final String username = connection.getUsername();
		final OpSession session = opSessionRegistrar.getSession(opId);
		final OpSynchronizedClock clock = session.getClock();
		final long nextTickStamp = clock.currentStamp();

		final SendOpInsertMessage message = new SendOpInsertMessage();
		message.setOpId(opId);
		message.setUser(username);
		message.setContent(content);
		message.setStart(start);
		message.setDocumentId(documentId);
		message.setTickStamp(nextTickStamp);
		cahootsSocket.send(message);
	}

	private void replace(final String opId, final String documentId,
			final int start, final int end, final String content) {
		final String username = connection.getUsername();
		final OpSession session = opSessionRegistrar.getSession(opId);
		final OpSynchronizedClock clock = session.getClock();
		final long nextTickStamp = clock.currentStamp();

		final SendOpReplaceMessage message = new SendOpReplaceMessage();
		message.setOpId(opId);
		message.setDocumentId(documentId);
		message.setStart(start);
		message.setEnd(end);
		message.setUser(username);
		message.setTickStamp(nextTickStamp);
		message.setContent(content);
		// TODO: Get old content
		message.setOldContent("");
		cahootsSocket.send(message);
	}

	private void delete(final String opId, final String documentId,
			final int start, final int end) {
		final String username = connection.getUsername();
		final OpSession session = opSessionRegistrar.getSession(opId);
		final OpSynchronizedClock clock = session.getClock();
		final long nextTickStamp = clock.currentStamp();

		final SendOpDeleteMessage message = new SendOpDeleteMessage();
		message.setOpId(opId);
		message.setDocumentId(documentId);
		message.setStart(start);
		message.setEnd(end);
		message.setUser(username);
		message.setTickStamp(nextTickStamp);
		// TODO: Get old content
		message.setOldContent("");
		cahootsSocket.send(message);
	}

	public void enableEvents() {
		enabled = true;
	}

	public void disableEvents() {
		enabled = false;
	}

	public class Share {
		private IDocument document;
		private IDocumentListener documentListener;
		private IncomingInsert incomingInsert;
		private IncomingDelete incomingDelete;
		private IncomingReplace incomingReplace;

		public Share(final IDocument document,
				final IDocumentListener documentListener,
				final IncomingDelete incomingDelete,
				final IncomingInsert incomingInsert,
				final IncomingReplace incomingReplace) {
			this.document = document;
			this.documentListener = documentListener;
			this.incomingDelete = incomingDelete;
			this.incomingInsert = incomingInsert;
			this.incomingReplace = incomingReplace;
		}

		public IncomingInsert getIncomingInsert() {
			return incomingInsert;
		}

		public void setIncomingInsert(final IncomingInsert incomingInsert) {
			this.incomingInsert = incomingInsert;
		}

		public IncomingDelete getIncomingDelete() {
			return incomingDelete;
		}

		public void setIncomingDelete(final IncomingDelete incomingDelete) {
			this.incomingDelete = incomingDelete;
		}

		public IncomingReplace getIncomingReplace() {
			return incomingReplace;
		}

		public void setIncomingReplace(final IncomingReplace incomingReplace) {
			this.incomingReplace = incomingReplace;
		}

		public IDocument getDocument() {
			return document;
		}

		public void setDocument(final IDocument document) {
			this.document = document;
		}

		public IDocumentListener getDocumentListener() {
			return documentListener;
		}

		public void setDocumentListener(final IDocumentListener documentListener) {
			this.documentListener = documentListener;
		}
	}

	public void inviteToShare(final String opId,
			final List<Collaborator> collaborators) {
		for (final Collaborator c : collaborators) {
			final InviteUserMessage message = new InviteUserMessage(
					connection.getUsername(), c.getUsername(), opId);

			cahootsSocket.send(message);
		}
	}
}
