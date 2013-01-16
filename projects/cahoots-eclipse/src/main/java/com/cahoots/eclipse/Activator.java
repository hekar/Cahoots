package com.cahoots.eclipse;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.cahoots.eclipse.guice.IndigoModule;
import com.cahoots.eclipse.guice.MainModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Activator extends AbstractUIPlugin {

	@SuppressWarnings("unused")
	private static Activator plugin;
	private static Injector injector;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		injector = Guice.createInjector(new MainModule(), new IndigoModule());
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	public static Injector getInjector() {
		return injector;
	}
	
}
