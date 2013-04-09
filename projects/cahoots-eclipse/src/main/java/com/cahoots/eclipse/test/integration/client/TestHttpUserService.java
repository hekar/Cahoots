package com.cahoots.eclipse.test.integration.client;

import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.http.CahootsHttpService;
import com.cahoots.connection.serialize.receive.ListUsersResponse;
import com.cahoots.connection.websocket.CahootsRealtimeClient;
import com.cahoots.eclipse.indigo.misc.TextEditorTools;

public class TestHttpUserService {

	private static ConnectionDetails connection;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connection = new ConnectionDetails();
		CahootsRealtimeClient socket = new CahootsRealtimeClient(connection,
				new WebSocketClientFactory(), new TextEditorTools());
		socket.connect("admin", "admin", "127.0.0.1:9000");
	}

	@Test
	public void testListUsers() {
		final CahootsHttpService service = new CahootsHttpService(connection);
		final ListUsersResponse usersResponse = service.listUsers();
		for (final String user : usersResponse.getUsers()) {
			System.out.println(user);
		}
	}

}
