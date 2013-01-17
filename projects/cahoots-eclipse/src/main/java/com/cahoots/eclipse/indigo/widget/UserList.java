package com.cahoots.eclipse.indigo.widget;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.cahoots.eclipse.Activator;
import com.google.inject.Injector;

public class UserList extends Composite {
	private TableViewer viewer;
	private UserListViewContentProvider source;

	class NameSorter extends ViewerSorter {
	}

	public UserList(final Composite parent, final int style) {
		super(parent, style);

		Injector injector = Activator.getInjector();
		source = injector.getInstance(UserListViewContentProvider.class);

		setLayout(new MigLayout("fill"));

		viewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(source);
		viewer.setLabelProvider(new UserListViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(null);

		source.addContentChangedListener(new SourceContentChangedListener() {
			@Override
			public void onEvent(Object msg) {
				viewer.refresh();
			}
		});

		viewer.refresh();
		viewer.getControl().setLayoutData("grow");
	}
}
