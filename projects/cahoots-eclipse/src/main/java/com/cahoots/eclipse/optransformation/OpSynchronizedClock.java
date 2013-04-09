package com.cahoots.eclipse.optransformation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.httpclient.NameValuePair;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.http.CahootsHttpClient;
import com.cahoots.connection.http.CahootsHttpMethodReturn;

public class OpSynchronizedClock {

	private static final String URL = "/op/clock";
	private final long startTime;

	@SuppressWarnings("deprecation")
	public static Future<OpSynchronizedClock> fromConnection(
			final CahootsHttpClient client, final CahootsConnection connection,
			final String opId) {

		if (!connection.isAuthenticated()) {
			throw new IllegalArgumentException(
					"Cahoots connection must be authenticated");
		}

		final List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new NameValuePair("auth_token", connection.getAuthToken()));
		data.add(new NameValuePair("opId", opId));

		final ExecutorService executorService = Executors.newCachedThreadPool();

		final Callable<OpSynchronizedClock> callable = new Callable<OpSynchronizedClock>() {
			@Override
			public OpSynchronizedClock call() throws Exception {
				final long start = System.currentTimeMillis();
				final CahootsHttpMethodReturn method = client.get(
						connection.getServer(), URL, data);
				final long latency = System.currentTimeMillis() - start;

				if (method.getStatusCode() == 200) {
					final String response = method.getMethod()
							.getResponseBodyAsString();
					final long clock = Long.parseLong(response);
					return new OpSynchronizedClock(start - (clock + (latency / 2)));
				} else {
					throw new RuntimeException("Invalid status code "
							+ method.getStatusCode());
				}
			}
		};

		final Future<OpSynchronizedClock> submit = executorService
				.submit(callable);

		return submit;
	}

	public OpSynchronizedClock(final long startTime) {
		this.startTime = startTime;
	}

	public long getClock() {
		return startTime;
	}

	public long currentStamp() {
		return System.currentTimeMillis() - startTime;
	}
}
