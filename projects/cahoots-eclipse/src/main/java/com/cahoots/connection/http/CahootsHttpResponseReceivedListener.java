package com.cahoots.connection.http;

import org.apache.commons.httpclient.HttpMethodBase;

public interface CahootsHttpResponseReceivedListener {

	void onReceive(int statusCode, HttpMethodBase method);

}
