package com.cahoots.connection.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;

import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.serialize.receive.ListUsersResponse;
import com.google.gson.Gson;

public class CahootsHttpService {

	private String authToken;
	private String domain;

	@Inject
	public CahootsHttpService(final ConnectionDetails connectionDetails) {
		this.authToken = connectionDetails.getAuthToken();
		this.domain = connectionDetails.getServer();
	}

	public ListUsersResponse listUsers() {
		final List<NameValuePair> data = new LinkedList<NameValuePair>();
		data.add(new NameValuePair("auth_token", authToken));

		final List<String> bodyResponse = new ArrayList<String>();
		new CahootsHttpClient().get(domain, "/user/list", data,
				new CahootsHttpResponseReceivedListener() {
					@Override
					public void onReceive(final int statusCode,
							final HttpMethodBase method) {
						try {
							bodyResponse.add(method.getResponseBodyAsString());
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				});

		if (bodyResponse.size() > 0) {
			final String json = bodyResponse.get(0);
			return new Gson().fromJson(json, ListUsersResponse.class);
		} else {
			throw new RuntimeException("No response received");
		}
	}

}
