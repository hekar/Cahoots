package com.cahoots.eclipse.indigo.log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.osgi.framework.Bundle;

import com.cahoots.eclipse.Activator;

public final class Log {

	public static void configureLogging(Bundle bundle) {
		try {
			URL url = bundle.getEntry(Activator.LOG_PROPERTIES);
			if (url == null) {
				throw new IOException(String.format("%s not found",
						Activator.LOG_PROPERTIES));
			}
	
			InputStream configFile = url.openStream();
	
			// Use properties to validate the file format
			new Properties().load(configFile);
	
			LogManager logManager = LogManager.getLogManager();
			logManager.readConfiguration(configFile);
		} catch (IOException ex) {
			System.out.println("WARNING: Could not open configuration file");
			System.out
					.println("WARNING: Logging not configured (console output only)");
		}
	}
	
	private static final Map<String, Log> loggers = new HashMap<String, Log>();
	
	public static <T> Log getLogger(Class<T> clazz) {
		return getLogger(clazz.getName());
	}

	public static Log getLogger(String name) {
		if (!loggers.containsKey(name)) {
			loggers.put(name, new Log(name));
		}

		return loggers.get(name);
	}

	private Logger logger;

	public Log(String name) {
		logger = Logger.getLogger(name);
	}

	public void info(String message, Object... args) {
		logger.log(Level.INFO, message, args);
	}

	public void warn(String message, Object... args) {
		logger.log(Level.WARNING, message, args);
	}

	public void error(String message, Object... args) {
		logger.log(Level.SEVERE, message, args);
	}

	public void verbose(String message, Object... args) {
		logger.log(Level.FINE, message, args);
	}

	public void debug(String message, Object... args) {
		logger.log(Level.FINER, message, args);
	}

}
