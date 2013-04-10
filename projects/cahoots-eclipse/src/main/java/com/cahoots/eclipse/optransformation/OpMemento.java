package com.cahoots.eclipse.optransformation;

import java.util.NoSuchElementException;
import java.util.TreeSet;

import com.cahoots.connection.serialize.receive.OpDeleteMessage;
import com.cahoots.connection.serialize.receive.OpInsertMessage;
import com.cahoots.connection.serialize.receive.OpReplaceMessage;
import com.cahoots.util.Log;

public class OpMemento {
	private final OpDocument document;
	private final TreeSet<OpTransformation> transformations;

	public OpMemento(final OpDocument document) {
		this.document = document;
		this.transformations = new TreeSet<OpTransformation>();
	}

	public OpDocument getDocument() {
		return document;
	}

	public synchronized void addTransformation(
			final OpTransformation transformation) {

		transformations.add(transformation);

		boolean found = false;
		for (final OpTransformation other : transformations) {
			if (other == transformation) {
				found = true;
				continue;
			}

			if (found) {
				int length = 0;
				if (transformation instanceof OpInsertMessage) {
					final OpInsertMessage opInsertMessage = (OpInsertMessage) transformation;
					length = opInsertMessage.getContent().length()
							- opInsertMessage.getStart();

				} else if (transformation instanceof OpReplaceMessage) {
					final OpReplaceMessage opReplaceMessage = (OpReplaceMessage) transformation;
					length = opReplaceMessage.getContent().length()
							- (opReplaceMessage.getEnd() - opReplaceMessage
									.getStart());

				} else if (transformation instanceof OpDeleteMessage) {
					final OpDeleteMessage opDeleteMessage = (OpDeleteMessage) transformation;
					length = opDeleteMessage.getEnd()
							- opDeleteMessage.getStart();
				}

				if (other instanceof OpInsertMessage) {
					final OpInsertMessage opInsertMessage = (OpInsertMessage) other;
					opInsertMessage.setStart(opInsertMessage.getStart()
							+ length);
				} else if (other instanceof OpReplaceMessage) {
					final OpReplaceMessage opReplaceMessage = (OpReplaceMessage) other;
					opReplaceMessage.setStart(opReplaceMessage.getStart()
							+ length);
					opReplaceMessage.setEnd(opReplaceMessage.getEnd() + length);
				} else if (other instanceof OpDeleteMessage) {
					final OpDeleteMessage opDeleteMessage = (OpDeleteMessage) other;
					opDeleteMessage.setStart(opDeleteMessage.getStart()
							+ length);
					opDeleteMessage.setEnd(opDeleteMessage.getEnd() + length);
				}
			}
		}

		transformation.setApplied(true);
	}

	public TreeSet<OpTransformation> getTransformations() {
		return transformations;
	}

	public long getLatestTimestamp() {
		try {
			return transformations.first().getTickStamp();
		} catch (final NoSuchElementException e) {
			return 0L;
		}
	}

	public synchronized String getContent() {
		final StringBuilder sb = new StringBuilder();
		
		for (final OpTransformation t : transformations) {
			System.out.println(String.format("%s: %d", t, t.getTickStamp()));
		}
		
		for (final OpTransformation transformation : transformations) {

			if (transformation instanceof OpInsertMessage) {
				final OpInsertMessage msg = (OpInsertMessage) transformation;
				final int start = msg.getStart();
				final String content = msg.getContent();
				Log.global().debug("%s, %d - %d", transformation, start);
				sb.insert(start, content);
				
			} else if (transformation instanceof OpReplaceMessage) {
				final OpReplaceMessage msg = (OpReplaceMessage) transformation;
				final int start = Math.min(msg.getStart(), sb.length());
				final int end = Math.min(msg.getEnd(), sb.length());
				final String content = msg.getContent();
				Log.global().debug("%s, %d - %d", transformation, start, end);
				sb.replace(start, end, content);

			} else if (transformation instanceof OpDeleteMessage) {
				final OpDeleteMessage msg = (OpDeleteMessage) transformation;
				final Integer start = msg.getStart();
				final int end = Math.min(msg.getEnd(), sb.length());
				Log.global().debug("%s, %d - %d", transformation, start, end);
				sb.delete(start, end);

			}
		}

		return sb.toString();
	}

}
