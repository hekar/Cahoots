package com.cahoots.eclipse.indigo.widget;

import java.util.List;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.cahoots.eclipse.Activator;
import com.cahoots.json.Collaborator;
import com.google.inject.Injector;

public class UserList extends Composite {

	private final TableViewer viewer;
	private final UserListViewContentProvider source;
	private final SourceContentChangedListener changedListener = new SourceContentChangedListener() {
		@Override
		public void onEvent(final Object msg) {
			viewer.refresh();
		}
	};

	class NameSorter extends ViewerSorter {
	}

	public UserList(final Composite parent, final int style) {
		super(parent, style);

		final Injector injector = Activator.getInjector();
		source = injector.getInstance(UserListViewContentProvider.class);

		setLayout(new MigLayout("fill"));

		viewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(source);
		viewer.setLabelProvider(new UserListViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(this);

		source.addContentChangedListener(changedListener);

		viewer.getControl().setLayoutData("grow");
	}

	@Override
	public void dispose() {
		source.removeContentChangedListener(changedListener);
		super.dispose();
	}

	@SuppressWarnings("unchecked")
	public List<Collaborator> getSelectedUsers() {
		if (viewer.getSelection() instanceof StructuredSelection) {
			final StructuredSelection structuredSelection = (StructuredSelection) viewer
					.getSelection();
			return structuredSelection.toList();
		} else {
			throw new IllegalStateException(
					"viewer selection is not StructuredSelection");
		}
	}

	public void addSelectionChangedListener(
			final ISelectionChangedListener listener) {
		viewer.addSelectionChangedListener(listener);
	}

	public void addDoubleClickListener(final IDoubleClickListener listener) {
		viewer.addDoubleClickListener(listener);
	}

}
