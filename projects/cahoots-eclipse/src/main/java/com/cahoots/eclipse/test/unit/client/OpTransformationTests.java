package com.cahoots.eclipse.test.unit.client;

import org.junit.Test;

import com.cahoots.eclipse.op.OpMemento;
import com.cahoots.eclipse.op.OpSynchronizedClock;
import com.cahoots.eclipse.op.OpTransformation;
import com.cahoots.json.receive.OpDeleteMessage;
import com.cahoots.json.receive.OpInsertMessage;
import com.cahoots.json.receive.OpReplaceMessage;

public class OpTransformationTests {

	final OpSynchronizedClock clock = new OpSynchronizedClock(0);

	@Test
	public void test() {
		final OpMemento memento = new OpMemento(null);

		final OpInsertMessage i = insertMessage();
		i.setTickStamp(5L);
		memento.addTransformation(i);

		final OpReplaceMessage r = replaceMessage();
		r.setTickStamp(9L);
		memento.addTransformation(r);

		final OpDeleteMessage d = deleteMessage();
		d.setTickStamp(7L);
		memento.addTransformation(d);
		
		final OpInsertMessage i1 = insertMessage();
		i1.setTickStamp(1L);
		memento.addTransformation(i1);

		final String content = memento.getContent();
		System.out.println(content);
		
		for (final OpTransformation transformation : memento.getTransformations()) {
			System.out.println(transformation.toString());
		}
	}

	private OpInsertMessage insertMessage() {
		final String username = "hello";
		final long nextTickStamp = clock.currentStamp();

		final OpInsertMessage i = new OpInsertMessage();
		i.setOpId("0");
		i.setUser(username);
		i.setContent("hello");
		i.setStart(0);
		i.setDocumentId("");
		i.setTickStamp(nextTickStamp);
		return i;
	}

	private OpReplaceMessage replaceMessage() {
		final String username = "hello";
		final OpSynchronizedClock clock = new OpSynchronizedClock(0);
		final long nextTickStamp = clock.currentStamp();

		final OpReplaceMessage r = new OpReplaceMessage();
		r.setOpId("0");
		r.setUser(username);
		r.setContent("hello world");
		r.setStart(5);
		r.setEnd(5);
		r.setDocumentId("");
		r.setTickStamp(nextTickStamp);
		return r;
	}

	private OpDeleteMessage deleteMessage() {
		final String username = "hello";
		final OpSynchronizedClock clock = new OpSynchronizedClock(0);
		final long nextTickStamp = clock.currentStamp();

		final OpDeleteMessage d = new OpDeleteMessage();
		d.setOpId("0");
		d.setUser(username);
		d.setStart(0);
		d.setEnd(5);
		d.setDocumentId("");
		d.setTickStamp(nextTickStamp);
		return d;
	}

}
