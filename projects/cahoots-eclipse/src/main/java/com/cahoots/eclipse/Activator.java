package com.cahoots.eclipse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.IllegalSelectorException;
import java.util.Properties;
import java.util.logging.LogManager;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.cahoots.eclipse.guice.IndigoModule;
import com.cahoots.eclipse.guice.MainModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Activator extends AbstractUIPlugin {

	private static final String LOG_PROPERTIES = "/log.properties";

	private static Activator activator;
	private static Injector injector;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		activator = this;

		configureLogging(context.getBundle());
		configureGuice();
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		super.stop(context);
		activator = null;
	}

	private void configureLogging(Bundle bundle) {
		try {
			URL url = bundle.getEntry(LOG_PROPERTIES);
			if (url == null) {
				throw new IOException(String.format("%s not found",
						LOG_PROPERTIES));
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

	private void configureGuice() {
		injector = Guice.createInjector(new MainModule(), new IndigoModule());
	}

	public static Activator getActivator() {
		if (activator == null) {
			throw new IllegalStateException(
					"Activator is null, when it should not be");
		}
		return activator;
	}

	public static Injector getInjector() {
		if (injector == null) {
			throw new IllegalStateException(
					"Injector is null, when it should not be");
		}
		return injector;
	}

}
