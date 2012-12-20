package com.cahoots.eclipse.test.integration.client;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cahoots.eclipse.Activator;
import com.cahoots.http.CahootsHttpService;
import com.cahoots.http.receive.ListUsersResponse;

public class TestHttpUserService {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Activator.initializeCahootsSocket();
		Activator.connect("admin", "admin", "127.0.0.1:9000");
	}

	@Test
	public void testListUsers() {
		final CahootsHttpService service = new CahootsHttpService("127.0.0.1:9000");
		final ListUsersResponse usersResponse = service.listUsers();
		for (final String user : usersResponse.getUsers()) {
			System.out.println(user);
		}
	}

}

