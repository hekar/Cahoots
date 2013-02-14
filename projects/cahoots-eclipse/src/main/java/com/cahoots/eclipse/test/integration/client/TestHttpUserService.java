package com.cahoots.eclipse.test.integration.client;

import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.http.CahootsHttpService;
import com.cahoots.connection.http.receive.ListUsersResponse;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.indigo.widget.TextEditorTools;

public class TestHttpUserService {

	private static CahootsConnection connection;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connection = new CahootsConnection();
		CahootsSocket socket = new CahootsSocket(connection,
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
