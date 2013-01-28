package com.cahoots.eclipse;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.cahoots.eclipse.event.EventRegistrarManager;
import com.cahoots.eclipse.guice.IndigoModule;
import com.cahoots.eclipse.guice.MainModule;
import com.cahoots.eclipse.indigo.log.Log;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Activator extends AbstractUIPlugin {

	public static final String LOG_PROPERTIES = "/src/main/java/log.properties";

	private static Activator activator;
	private static Injector injector;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		activator = this;

		Log.configureLogging(context.getBundle());
		configureGuice();
		configureRegistrar();
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		super.stop(context);
		activator = null;
	}

	private void configureGuice() {
		injector = Guice.createInjector(new MainModule(), new IndigoModule());
	}

	private void configureRegistrar() {
		final EventRegistrarManager eventRegistrarManager = injector
				.getInstance(EventRegistrarManager.class);
		eventRegistrarManager.registerAllEvents();
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
