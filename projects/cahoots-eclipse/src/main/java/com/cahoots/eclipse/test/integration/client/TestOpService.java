package com.cahoots.eclipse.test.integration.client;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cahoots.eclipse.Activator;
import com.cahoots.events.OpDeleteEventListener;
import com.cahoots.events.OpInsertEventListener;
import com.cahoots.events.OpReplaceEventListener;
import com.cahoots.events.ShareDocumentEventListener;
import com.cahoots.json.receive.OpDeleteMessage;
import com.cahoots.json.receive.OpInsertMessage;
import com.cahoots.json.receive.OpReplaceMessage;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.send.SendOpDeleteMessage;
import com.cahoots.json.send.SendOpInsertMessage;
import com.cahoots.json.send.SendOpReplaceMessage;
import com.cahoots.json.send.SendShareDocumentMessage;
import com.cahoots.websocket.CahootsSocket;

public class TestOpService {

	private static CahootsSocket socket;
	@BeforeClass
	public static void classSetUp() throws Exception {
		Activator.initializeCahootsSocket();
		Activator.connect("admin", "admin", "127.0.0.1:9000");
		socket = CahootsSocket.getInstance();
	}

	@Test
	public void testShareDocument() throws Exception {
		shareDocument();
	}

	private ShareDocumentMessage shareDocument() {
		final SendShareDocumentMessage shareDocumentMessage = new SendShareDocumentMessage(
				"admin", "cahoots-eclipse/README.txt", Arrays.asList("admin"));
		return socket.sendAndWaitForResponse(shareDocumentMessage,
				ShareDocumentMessage.class, ShareDocumentEventListener.class);
	}

	@Test
	public void testOpInsert() {
		final ShareDocumentMessage document = shareDocument();
		
		final SendOpInsertMessage op = new SendOpInsertMessage();
		op.setOpId(document.getOpId());
		op.setDocumentId(document.getDocumentId());
		op.setStart(0);
		op.setTickStamp(0L);
		op.setContents("Testing insert 123");
		
		socket.sendAndWaitForResponse(op,
				OpInsertMessage.class, OpInsertEventListener.class);
	}

	@Test
	public void testOpReplace() {
		final ShareDocumentMessage document = shareDocument();
		
		final String contents = "Testing replace 123";
		
		final SendOpReplaceMessage op = new SendOpReplaceMessage();
		op.setOpId(document.getOpId());
		op.setStart(0);
		op.setEnd(contents.length());
		op.setTickStamp(0L);
		op.setContents(contents);

		socket.sendAndWaitForResponse(op,
				OpReplaceMessage.class, OpReplaceEventListener.class);
	}

	@Test
	public void testOpDelete() {
		final ShareDocumentMessage document = shareDocument();
		
		final SendOpDeleteMessage op = new SendOpDeleteMessage();
		op.setOpId(document.getOpId());
		op.setStart(0);
		op.setEnd(1);
		op.setTickStamp(0L);
		
		socket.sendAndWaitForResponse(op,
				OpDeleteMessage.class, OpDeleteEventListener.class);
	}

}
