package com.cahoots.eclipse.test.integration.client;

import java.util.Arrays;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.http.CahootsHttpClient;
import com.cahoots.connection.serialize.receive.OpDeleteMessage;
import com.cahoots.connection.serialize.receive.OpInsertMessage;
import com.cahoots.connection.serialize.receive.OpReplaceMessage;
import com.cahoots.connection.serialize.receive.ShareDocumentMessage;
import com.cahoots.connection.serialize.send.SendOpDeleteMessage;
import com.cahoots.connection.serialize.send.SendOpInsertMessage;
import com.cahoots.connection.serialize.send.SendOpReplaceMessage;
import com.cahoots.connection.serialize.send.SendShareDocumentMessage;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.indigo.misc.TextEditorTools;
import com.cahoots.eclipse.op.OpSynchronizedClock;
import com.cahoots.event.OpDeleteEventListener;
import com.cahoots.event.OpInsertEventListener;
import com.cahoots.event.OpReplaceEventListener;
import com.cahoots.event.ShareDocumentEventListener;

public class TestOpService {

	private static CahootsSocket socket;
	private static CahootsConnection cahootsConnection;

	@BeforeClass
	public static void classSetUp() throws Exception {
		cahootsConnection = new CahootsConnection();
		socket = new CahootsSocket(cahootsConnection,
				new WebSocketClientFactory(), new TextEditorTools());
		socket.connect("admin", "admin", "127.0.0.1:9000");
	}

	@Test
	public void testShareDocument() throws Exception {
		shareDocument();
	}

	@SuppressWarnings("deprecation")
	private ShareDocumentMessage shareDocument() {
		final SendShareDocumentMessage shareDocumentMessage = new SendShareDocumentMessage(
				"admin", "cahoots-eclipse/README.txt", Arrays.asList("admin"),
				"");
		return socket.sendAndWaitForResponse(shareDocumentMessage,
				ShareDocumentMessage.class, ShareDocumentEventListener.class);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testOpInsert() {
		final ShareDocumentMessage document = shareDocument();

		final SendOpInsertMessage op = new SendOpInsertMessage();
		op.setOpId(document.getOpId());
		op.setDocumentId(document.getDocumentId());
		op.setStart(0);
		op.setTickStamp(0L);
		op.setContent("Testing insert 123");

		socket.sendAndWaitForResponse(op, OpInsertMessage.class,
				OpInsertEventListener.class);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testOpReplace() {
		final ShareDocumentMessage document = shareDocument();

		final String content = "Testing replace 123";

		final SendOpReplaceMessage op = new SendOpReplaceMessage();
		op.setOpId(document.getOpId());
		op.setStart(0);
		op.setEnd(content.length());
		op.setTickStamp(0L);
		op.setContent(content);

		socket.sendAndWaitForResponse(op, OpReplaceMessage.class,
				OpReplaceEventListener.class);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testOpDelete() {
		final ShareDocumentMessage document = shareDocument();

		final SendOpDeleteMessage op = new SendOpDeleteMessage();
		op.setOpId(document.getOpId());
		op.setStart(0);
		op.setEnd(1);
		op.setTickStamp(0L);

		socket.sendAndWaitForResponse(op, OpDeleteMessage.class,
				OpDeleteEventListener.class);
	}

	@Test
	public void testSynchronizedClock() throws Exception {
		final ShareDocumentMessage document = shareDocument();

		final Future<OpSynchronizedClock> clockFuture = OpSynchronizedClock
				.fromConnection(new CahootsHttpClient(), cahootsConnection,
						document.getOpId());
		final OpSynchronizedClock clock = clockFuture.get();

		System.out.println(clock.getClock());
	}
}
