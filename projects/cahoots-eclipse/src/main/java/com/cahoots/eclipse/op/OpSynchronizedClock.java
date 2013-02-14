package com.cahoots.eclipse.op;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.httpclient.NameValuePair;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.http.tools.CahootsHttpClient;
import com.cahoots.connection.http.tools.CahootsHttpMethodReturn;

public class OpSynchronizedClock {

	private final long startTime;

	public static Future<OpSynchronizedClock> fromConnection(
			final CahootsHttpClient client, final CahootsConnection connection) {

		final List<NameValuePair> data = new ArrayList<NameValuePair>();
		final ExecutorService executorService = Executors.newCachedThreadPool();

		final Callable<OpSynchronizedClock> callable = new Callable<OpSynchronizedClock>() {
			@Override
			public OpSynchronizedClock call() throws Exception {
				final CahootsHttpMethodReturn method = client.get(
						connection.getServer(), "/op/clock", data);

				if (method.getStatusCode() == 200) {
					final String response = method.getMethod()
							.getResponseBodyAsString();
					final int clock = Integer.parseInt(response);
					return new OpSynchronizedClock(clock);
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
