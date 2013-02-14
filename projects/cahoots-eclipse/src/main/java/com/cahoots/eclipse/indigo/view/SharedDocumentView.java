package com.cahoots.eclipse.indigo.view;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.cahoots.eclipse.collab.share.ShareDocumentSessionUnRegisterListener;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.receive.UnShareDocumentMessage;
import com.google.inject.Injector;

public class SharedDocumentView extends ViewPart {

	public static final String ID = "com.cahoots.eclipse.indigo.view.SharedDocumentView";

	private TableViewer viewer;

	private ShareDocumentSessionManager shareDocumentSessionManager;

	class ViewContentProvider implements IStructuredContentProvider {
		private List<ShareDocumentMessage> elements = new ArrayList<ShareDocumentMessage>();

		public void inputChanged(final Viewer v, final Object oldInput,
				final Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(final Object parent) {
			return elements.toArray();
		}

		public void add(final ShareDocumentMessage message) {
			elements.add(message);
		}

		public void remove(final UnShareDocumentMessage message) {
			for (final Iterator<ShareDocumentMessage> it = elements.iterator(); it
					.hasNext();) {
				final ShareDocumentMessage sdm = it.next();

				final boolean equals = sdm.getDocumentId().equals(
						message.getDocumentId())
						&& sdm.getOpId().equals(message.getOpId());
				if (equals) {
					it.remove();
				}
			}
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(final Object obj, final int index) {
			return getText(obj);
		}

		public Image getColumnImage(final Object obj, final int index) {
			return getImage(obj);
		}

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

		final ShareDocumentSessionUnRegisterListener unregisterListener = new ShareDocumentSessionUnRegisterListener() {
			@Override
			public void onEvent(final UnShareDocumentMessage msg) {
				source.remove(msg);
			}
		};

		shareDocumentSessionManager.addOnRegisterListener(registerListener);
		shareDocumentSessionManager.addOnUnRegisterListener(unregisterListener);
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
