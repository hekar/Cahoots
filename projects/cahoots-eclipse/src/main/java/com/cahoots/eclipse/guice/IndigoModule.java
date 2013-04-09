package com.cahoots.eclipse.guice;

import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.osgi.framework.Bundle;

import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.indigo.misc.CollaborationsViewContentProvider;
import com.cahoots.eclipse.indigo.misc.UserListViewContentProvider;
import com.cahoots.eclipse.indigo.popup.ConnectStuff;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class IndigoModule implements Module {

	@Override
	public void configure(final Binder binder) {
		binder.bind(Activator.class).toInstance(Activator.getActivator());
		binder.bind(IWorkbench.class).toInstance(
				Activator.getActivator().getWorkbench());
		binder.bind(IWorkbenchWindow.class).toInstance(
				Activator.getActivator().getWorkbench()
						.getActiveWorkbenchWindow());
		binder.bind(IEditorRegistry.class).toInstance(
				Activator.getActivator().getWorkbench().getEditorRegistry());
		binder.bind(UserListViewContentProvider.class).in(Singleton.class);
		binder.bind(CollaborationsViewContentProvider.class)
				.in(Singleton.class);
		binder.bind(Bundle.class).toInstance(
				Activator.getActivator().getBundle());
		binder.bind(ConnectStuff.class).in(Singleton.class);
	}
}
