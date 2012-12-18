package com.cahoots.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;

import com.cahoots.eclipse.Activator;
import com.cahoots.http.receive.ListUsersResponse;
import com.cahoots.http.tools.CahootsHttpClient;
import com.cahoots.http.tools.CahootsHttpResponseReceivedListener;
import com.google.gson.Gson;

public class CahootsHttpService {

	private String domain;
	public CahootsHttpService(final String domain) {
		this.domain = domain;
	}
	
	public ListUsersResponse listUsers() {
		final String token = Activator.getAuthToken();
		final List<NameValuePair> data = new LinkedList<NameValuePair>();
		data.add( new NameValuePair("auth_token", token));
		
		final List<String> bodyResponse = new ArrayList<String>();
		new CahootsHttpClient().get(domain, "/user/list", data, new CahootsHttpResponseReceivedListener() {
			@Override
			public void onReceive(final int statusCode, final HttpMethodBase method) {
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
