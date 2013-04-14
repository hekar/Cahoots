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

import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.http.CahootsHttpClient;
import com.cahoots.connection.serialize.Collaborator;
import com.cahoots.connection.serialize.receive.OpDeleteMessage;
import com.cahoots.connection.serialize.receive.OpInsertMessage;
import com.cahoots.connection.serialize.send.InviteUserMessage;
import com.cahoots.connection.serialize.send.SendOpDeleteMessage;
import com.cahoots.connection.serialize.send.SendOpInsertMessage;
import com.cahoots.connection.serialize.send.SendShareDocumentMessage;
import com.cahoots.connection.websocket.CahootsRealtimeClient;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.indigo.misc.SwtDisplayUtils;
import com.cahoots.eclipse.optransformation.OpDocument;
import com.cahoots.eclipse.optransformation.OpMergeManager;
import com.cahoots.eclipse.optransformation.OpSession;
import com.cahoots.eclipse.optransformation.OpSessionRegister;
import com.cahoots.eclipse.optransformation.OpSynchronizedClock;

public class ShareDocumentManager {

	private boolean enabled = true;

	private final CahootsRealtimeClient cahootsSocket;
	private final OpSessionRegister opSessionRegistrar;
	private final ConnectionDetails connectionDetails;
	private final CahootsHttpClient cahootsHttpClient;
	private final Map<String, ShareDocumentManager.Share> shares = new HashMap<String, ShareDocumentManager.Share>();
	private final Map<String, String> documentIds = new HashMap<String, String>();

	@Inject
	public ShareDocumentManager(final ConnectionDetails connectionDetails,
			final CahootsHttpClient cahootsHttpClient,
			final CahootsRealtimeClient cahootsSocket,
			final OpSessionRegister opSessionManager) {
		this.connectionDetails = connectionDetails;
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
				connectionDetails.getUsername(), documentId, collaboratorUsernames,
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

	public void addDocumentListener(final OpDocument document) {
		try {
			
			final String documentId = document.getDocumentId();
			final String opId = document.getOpId();
			final ITextEditor textEditor = document.getTextEditor();

			final IncomingInsert insert = new IncomingInsert(
					opSessionRegistrar, this, connectionDetails, textEditor,
					documentId, opId);
			final IncomingReplace replace = new IncomingReplace(
					opSessionRegistrar, this, connectionDetails, textEditor,
					documentId, opId);
			final IncomingDelete delete = new IncomingDelete(
					opSessionRegistrar, this, connectionDetails, textEditor,
					documentId, opId);

			cahootsSocket.addOpInsertEventListener(insert);
			cahootsSocket.addOpReplaceEventListener(replace);
			cahootsSocket.addOpDeleteEventListener(delete);

			documentIds.put(opId, documentId);
			final OpSynchronizedClock clock = OpSynchronizedClock
					.fromConnection(cahootsHttpClient, connectionDetails, opId).get();

			final OpMergeManager opMemento = new OpMergeManager(document, connectionDetails);
			final boolean initiated = connectionDetails.isLoggedInUser(connectionDetails.getUsername());
			final OpSession opSession = new OpSession(initiated, 
					opMemento, clock);
			opSessionRegistrar.addSession(opId, opSession);

			final IDocumentListener documentListener = new IDocumentListener() {

				/**
				 * Handle insert operation
				 */
				@Override
				public void documentChanged(final DocumentEvent event) {
					final Runnable runnable = new Runnable() {
						
						@Override
						public void run() {
							try {
								if (enabled) {
									synchronized (Activator.globalLock) {
										final int length = event.fLength;
										final int start = event.fOffset;
										final String text = event.getText();
										if (length > 0 && text.isEmpty()) {
											delete(document, start, length, oldContent);
										} else if (length > 0 && !text.isEmpty()) {
											replace(document, start, length, text, oldContent);
										} else if (length == 0 && !text.isEmpty()) {
											insert(document, start, text);
										} else {
											// ???
										}
									}
								}
							} catch (final Exception e) {
								e.printStackTrace();
							}
						}
					};
					
					SwtDisplayUtils.sync(runnable);
				}

				private String oldContent = "";
				@Override
				public void documentAboutToBeChanged(final DocumentEvent event) {
					try {
						oldContent = event.getDocument().get(event.getOffset(), event.getLength());
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			};
			shares.put(opId, new Share(document.getDocument(), documentListener, delete,
					insert, replace));
			document.getDocument().addDocumentListener(documentListener);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final ExecutionException e) {
			e.printStackTrace();
		}
	}

	public String getDocumentId(final String opId) {
		return documentIds.get(opId);
	}

	private void insert(final OpDocument document, final int start, final String content) {
		final String username = connectionDetails.getUsername();
		final OpSession session = opSessionRegistrar.getSession(document.getOpId());
		final OpSynchronizedClock clock = session.getClock();
		final long nextTickStamp = clock.currentStamp();

		final OpInsertMessage message = new SendOpInsertMessage();
		message.setOpId(document.getOpId());
		message.setUser(username);
		message.setContent(content);
		message.setStart(start);
		message.setReplacementLength(0);
		message.setDocumentId(document.getDocumentId());
		message.setTickStamp(nextTickStamp);
		
		final OpMergeManager memento = session.getMemento();
		memento.sendTransformation(message);

		cahootsSocket.send(message);
	}

	private void replace(final OpDocument document, final int start, final int length,
			final String content, final String oldContent) {
		final String username = connectionDetails.getUsername();
		final String opId = document.getOpId();
		final String documentId = document.getDocumentId();
		final OpSession session = opSessionRegistrar.getSession(opId);
		final OpSynchronizedClock clock = session.getClock();
		final long nextTickStamp = clock.currentStamp();

		final OpMergeManager memento = session.getMemento();

		final OpDeleteMessage deleteMessage = new SendOpDeleteMessage();
		deleteMessage.setOpId(opId);
		deleteMessage.setDocumentId(documentId);
		deleteMessage.setStart(start);
		deleteMessage.setReplacementLength(length);
		deleteMessage.setUser(username);
		deleteMessage.setTickStamp(nextTickStamp);
		deleteMessage.setOldContent(oldContent);

		final OpInsertMessage insertMessage = new SendOpInsertMessage();
		insertMessage.setOpId(opId);
		insertMessage.setDocumentId(documentId);
		insertMessage.setStart(start);
		insertMessage.setReplacementLength(0);
		insertMessage.setUser(username);
		insertMessage.setTickStamp(nextTickStamp);
		insertMessage.setContent(content);
		
		memento.sendTransformation(deleteMessage);
		memento.sendTransformation(insertMessage);
		
		cahootsSocket.send(deleteMessage);
		cahootsSocket.send(insertMessage);
	}

	private void delete(final OpDocument document, final int start, final int length, final String oldContent) {
		final String username = connectionDetails.getUsername();
		final OpSession session = opSessionRegistrar.getSession(document.getOpId());
		final OpSynchronizedClock clock = session.getClock();
		final long nextTickStamp = clock.currentStamp();

		final OpDeleteMessage message = new SendOpDeleteMessage();
		message.setOpId(document.getOpId());
		message.setDocumentId(document.getDocumentId());
		message.setStart(start);
		message.setReplacementLength(length);
		message.setUser(username);
		message.setTickStamp(nextTickStamp);
		message.setOldContent(oldContent);

		final OpMergeManager memento = session.getMemento();
		memento.sendTransformation(message);
		
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
					connectionDetails.getUsername(), c.getUsername(), opId);

			cahootsSocket.send(message);
		}
	}
}
