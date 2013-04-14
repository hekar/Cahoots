package com.cahoots.connection.http;

import org.apache.commons.httpclient.methods.GetMethod;

/**
 * 
 */
public class CahootsHttpMethodReturn {

	private final int statusCode;
	private final GetMethod method;

	public CahootsHttpMethodReturn(final int statusCode, final GetMethod method) {
		this.statusCode = statusCode;
		this.method = method;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public GetMethod getMethod() {
		return method;
	}

}
