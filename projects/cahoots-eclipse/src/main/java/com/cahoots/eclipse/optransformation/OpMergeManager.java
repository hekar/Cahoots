package com.cahoots.eclipse.optransformation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import com.cahoots.connection.ConnectionDetails;
import com.cahoots.util.ExceptionUtils;
import com.cahoots.util.Tracer;

/**
 * Manages merging of operational transformations, so that the document may be synchronized 
 */
public class OpMergeManager {
	private final OpDocument document;
	private final List<OpTransformation> unackLocal;

	private static Object lock = new Object();
	private Tracer out;

	private int localTransformations = 0;
	private int remoteTransformations = 0;

	public OpMergeManager(final OpDocument document, final ConnectionDetails connectionDetails) {
		this.document = document;
		unackLocal = new ArrayList<OpTransformation>();
		out = new Tracer(connectionDetails.getUsername());
	}

	public OpDocument getDocument() {
		return document;
	}

	/**
	 * Perform conflict resolution and merge transformations
	 * @param edoc
	 * @param remote
	 */
	private void mergeTransformation(final IDocument edoc, final OpTransformation remote) {
		synchronized (lock) {
			if (remote.isInitialReplace()) {
				try {
					edoc.replace(0, edoc.getLength(), remote.getContent());
				} catch (final BadLocationException e) {
					ExceptionUtils.rethrow(e);
				}
			} else {
				cleanUnAcknowledgedTransformations(remote);

				out.writeln(remote.toString());
				out.flush();

				if (!unackLocal.isEmpty()) {
					// On the wire conflicts
					out.writeln("Remote Before: %s", remote);
					out.writeln("============ CONFLICT ==============");
					for (final OpTransformation local : unackLocal) {
						out.writeln("Local       : %s", local);
						new OpConflictStrategy(out).mergeConflicts(document, remote, local);
					}
					out.writeln("Remote After: %s", remote);
				}

				out.writeln("============ BEFORE ==============");
				out.writeln(getDocumentContent());
				apply(remote);
				out.writeln("============ AFTER  ==============");
				out.writeln(getDocumentContent());

				remote.setApplied(true);
			}
		}
	}

	/**
	 * Remove local transformations older than the remote from the queue
	 * @param remote
	 */
	private void cleanUnAcknowledgedTransformations(final OpTransformation remote) {
		if (!unackLocal.isEmpty()) {
			for (final Iterator<OpTransformation> it = unackLocal.iterator(); it.hasNext();) {
				final OpTransformation local = it.next();
				if (remote.getRemoteCount() > local.getLocalCount()) {
					it.remove();
				}
			}
		}
	}

	/**
	 * Update a local transformation for sending
	 * 
	 * @param message
	 */
	public void sendTransformation(final OpTransformation message) {
		unackLocal.add(message);
		message.setLocalCount(localTransformations);
		message.setRemoteCount(remoteTransformations);
		out.writeln("Sent: %s", message);

		if (!message.isInitialReplace()) {
			localTransformations++;
		}
	}

	/**
	 * Receive an incoming transformation
	 * 
	 * @param message
	 */
	public void receiveTransformation(final OpTransformation message) {
		mergeTransformation(document.getDocument(), message);
		if (!message.isInitialReplace()) {
			remoteTransformations++;
		}
	}

	/**
	 * Apply a transformation to the document
	 * 
	 * @param message
	 */
	public void apply(final OpTransformation message) {
		try {
			final IDocument edoc = getDocument().getDocument();
			final int start = Math.min(message.getIndex(), edoc.getLength() - message.getReplacementLength());
			final int replacementLength = message.getReplacementLength();
			final String content = message.getContent();
			edoc.replace(start, replacementLength, content);
		} catch (final BadLocationException e) {
			ExceptionUtils.rethrow(e);
		}
	}
	
	public String getDocumentContent() {
		return document.getDocument().get();
	}
}
