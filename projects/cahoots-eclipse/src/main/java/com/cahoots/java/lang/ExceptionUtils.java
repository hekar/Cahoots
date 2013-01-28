package com.cahoots.java.lang;

public class ExceptionUtils {
	public static void rethrow(Exception e) {
		throw new RuntimeException(e);
	}
	
	public static RuntimeException toRuntime(Exception e) {
		return new RuntimeException(e);
	}
}
