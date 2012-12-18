package com.cahoots.http.tools;

import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class CahootsHttpClient {

	public void get(final String server, final String path, final List<NameValuePair> data,
			final CahootsHttpResponseReceivedListener listener) {
		final HttpClient client = new HttpClient();
		final GetMethod method = new GetMethod("http://" + server + path);

		method.setQueryString(data.toArray(new NameValuePair[] {}));
		try
		{
			final int statusCode = client.executeMethod(method);
			listener.onReceive(statusCode, method);
		} catch (final Exception ex)
		{
			throw new RuntimeException("Error connecting to server", ex);
		}
	}

	public void post(final String server, final String path, final List<NameValuePair> data,
			final CahootsHttpResponseReceivedListener listener) {
		final HttpClient client = new HttpClient();
		final PostMethod method = new PostMethod("http://" + server + path);

		method.setRequestBody(data.toArray(new NameValuePair[data.size()]));
		try
		{
			final int statusCode = client.executeMethod(method);
			listener.onReceive(statusCode, method);
		} catch (final Exception ex)
		{
			throw new RuntimeException("Error connecting to server", ex);
		}
	}

}
