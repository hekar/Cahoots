package com.cahoots.eclipse.op;

import java.util.NoSuchElementException;
import java.util.TreeSet;

import com.cahoots.serialize.json.receive.OpDeleteMessage;
import com.cahoots.serialize.json.receive.OpInsertMessage;
import com.cahoots.serialize.json.receive.OpReplaceMessage;

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
					length = opInsertMessage.getContent().length() - opInsertMessage.getStart();
					
				} else if (transformation instanceof OpReplaceMessage) {
					final OpReplaceMessage opReplaceMessage = (OpReplaceMessage) transformation;
					length = (opReplaceMessage.getContent().length() - opReplaceMessage.getStart()) -
							(opReplaceMessage.getEnd() - opReplaceMessage.getStart());
					
				} else if (transformation instanceof OpDeleteMessage) {
					final OpDeleteMessage opDeleteMessage = (OpDeleteMessage) transformation;
					length = opDeleteMessage.getEnd() - opDeleteMessage.getStart();
				}
				
				
				if (other instanceof OpInsertMessage) {
					final OpInsertMessage opInsertMessage = (OpInsertMessage) other;
					opInsertMessage.setStart(opInsertMessage.getStart() + length);
				} else if (other instanceof OpReplaceMessage) {
					final OpReplaceMessage opReplaceMessage = (OpReplaceMessage) other;
					opReplaceMessage.setStart(opReplaceMessage.getStart() + length);
					opReplaceMessage.setEnd(opReplaceMessage.getEnd() + length);
				} else if (other instanceof OpDeleteMessage) {
					final OpDeleteMessage opDeleteMessage = (OpDeleteMessage) other;
					opDeleteMessage.setStart(opDeleteMessage.getStart() + length);
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
		for (final OpTransformation transformation : transformations) {
			
			if (transformation instanceof OpInsertMessage) {
				final OpInsertMessage msg = (OpInsertMessage) transformation;
				sb.insert(msg.getStart(), msg.getContent());
				
			} else if (transformation instanceof OpReplaceMessage) {
				final OpReplaceMessage msg = (OpReplaceMessage) transformation;
				sb.replace(Math.min(msg.getStart(), sb.length()), 
						Math.min(msg.getEnd(), sb.length()), msg.getContent());
				
			} else if (transformation instanceof OpDeleteMessage) {
				final OpDeleteMessage msg = (OpDeleteMessage) transformation;
				sb.delete(msg.getStart(), 
						Math.min(msg.getEnd(), sb.length()));
				
			}
		}
		
		return sb.toString();
	}

}
