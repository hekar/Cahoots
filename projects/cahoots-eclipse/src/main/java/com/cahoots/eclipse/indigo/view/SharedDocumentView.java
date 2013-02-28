package com.cahoots.eclipse.indigo.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.share.ShareDocumentSessionManager;
import com.cahoots.eclipse.collab.share.ShareDocumentSessionRegisterListener;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.google.inject.Injector;

public class SharedDocumentView extends ViewPart {

	public static final String ID = "com.cahoots.eclipse.indigo.view.SharedDocumentView";

	private TableViewer viewer;

	private ShareDocumentSessionManager shareDocumentSessionManager;

	class ViewContentProvider implements IStructuredContentProvider {
		private List<ShareDocumentMessage> elements = new ArrayList<ShareDocumentMessage>();

		@Override
		public void inputChanged(final Viewer v, final Object oldInput,
				final Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(final Object parent) {
			return elements.toArray();
		}

		public void add(final ShareDocumentMessage message) {
			elements.add(message);
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public String getColumnText(final Object obj, final int index) {
			return getText(obj);
		}

		@Override
		public Image getColumnImage(final Object obj, final int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(final Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	class NameSorter extends ViewerSorter {
	}

	public SharedDocumentView() {
		final Injector injector = Activator.getInjector();
		shareDocumentSessionManager = injector
				.getInstance(ShareDocumentSessionManager.class);
	}

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		final ViewContentProvider source = new ViewContentProvider();
		viewer.setContentProvider(source);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		final ShareDocumentSessionRegisterListener registerListener = new ShareDocumentSessionRegisterListener() {
			@Override
			public void onEvent(final ShareDocumentMessage msg) {
				source.add(msg);
			}
		};

		shareDocumentSessionManager.addOnRegisterListener(registerListener);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
