package com.cahoots.eclipse.guice;

import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.event.EventRegistrarManager;
import com.cahoots.eclipse.indigo.widget.UserListViewContentProvider;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class IndigoModule implements Module {

	@Override
	public void configure(final Binder binder) {
		binder.bind(Activator.class).toInstance(Activator.getActivator());
		binder.bind(UserListViewContentProvider.class).in(Singleton.class);
		binder.bind(EventRegistrarManager.class).in(Singleton.class);
	}

}
