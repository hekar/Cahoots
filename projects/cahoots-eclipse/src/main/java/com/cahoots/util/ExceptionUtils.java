package com.cahoots.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionUtils {
	public static void rethrow(final Exception e) {
		throw new RuntimeException(e);
	}

	public static RuntimeException toRuntime(final Exception e) {
		return new RuntimeException(e);
	}

	public static String getStackTrace(final Throwable throwable) {
		final Writer writer = new StringWriter();

		final PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);

		final String message = writer.toString();
		return message;
	}
}
