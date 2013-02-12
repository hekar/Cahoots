package com.cahoots.eclipse.indigo.log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.httpclient.util.ExceptionUtil;
import org.osgi.framework.Bundle;

import com.cahoots.eclipse.Activator;
import com.cahoots.java.lang.ExceptionUtils;

public final class Log {

	public static void configureLogging(final Bundle bundle) {
		try {
			final URL url = bundle.getEntry(Activator.LOG_PROPERTIES);
			if (url == null) {
				throw new IOException(String.format("%s not found",
						Activator.LOG_PROPERTIES));
			}

			final InputStream configFile = url.openStream();

			// Use properties to validate the file format
			new Properties().load(configFile);

			final LogManager logManager = LogManager.getLogManager();
			logManager.readConfiguration(configFile);
		} catch (final IOException ex) {
			System.out.println("WARNING: Could not open configuration file");
			System.out
					.println("WARNING: Logging not configured (console output only)");
		}
	}

	private static final Map<String, Log> loggers = new HashMap<String, Log>();

	public static <T> Log getLogger(final Class<T> clazz) {
		return getLogger(clazz.getName());
	}

	public static Log getLogger(final String name) {
		if (!loggers.containsKey(name)) {
			loggers.put(name, new Log(name));
		}

		return loggers.get(name);
	}

	private Logger logger;

	public Log(final String name) {
		logger = Logger.getLogger(name);
	}

	public void info(final String message, final Object... args) {
		logger.log(Level.INFO, message, args);
	}

	public void warn(final String message, final Object... args) {
		logger.log(Level.WARNING, message, args);
	}

	public void error(final String message, final Object... args) {
		logger.log(Level.SEVERE, message, args);
	}

	public void verbose(final String message, final Object... args) {
		logger.log(Level.FINE, message, args);
	}

	public void debug(final String message, final Object... args) {
		logger.log(Level.FINER, message, args);
	}

	public void info(final Exception e) {
		final String message = ExceptionUtils.getStackTrace(e);
		info(message);
	}

	public void warn(final Exception e) {
		final String message = ExceptionUtils.getStackTrace(e);
		warn(message);
	}

	public void error(final Exception e) {
		final String message = ExceptionUtils.getStackTrace(e);
		error(message);
	}

	public void verbose(final Exception e) {
		final String message = ExceptionUtils.getStackTrace(e);
		verbose(message);
	}
}
