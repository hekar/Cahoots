package com.cahoots.system.log;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

	private static Map<String, Log> loggers = new HashMap<String, Log>();

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
