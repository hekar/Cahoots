package com.cahoots.eclipse.indigo.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.cahoots.chat.Chat;
import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.serialize.Collaboration;
import com.cahoots.connection.serialize.Collaborator;
import com.cahoots.connection.serialize.send.LeaveCollaborationMessage;
import com.cahoots.connection.websocket.CahootsRealtimeClient;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.share.InviteDocumentDialog;
import com.cahoots.eclipse.collab.share.ShareDocumentManager;
import com.cahoots.eclipse.indigo.misc.CollaborationsViewContentProvider;
import com.cahoots.eclipse.indigo.misc.CollaboratorsViewLabelProvider;
import com.cahoots.eclipse.indigo.misc.SourceContentChangedListener;
import com.google.inject.Injector;

public class CollaborationsView extends ViewPart {

	public static final String ID = "com.cahoots.eclipse.indigo.view.CollaborationsView";

	private CahootsRealtimeClient socket;

	private ConnectionDetails connection;

	private TableViewer viewer;
	private CollaborationsViewContentProvider source;
	private ShareDocumentManager sharedDocumentManager;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;

	private final SourceContentChangedListener listener = new SourceContentChangedListener() {
		@Override
		public void onEvent(final Object msg) {
			viewer.refresh();
		}
	};

	class NameSorter extends ViewerSorter {
	}

	public CollaborationsView() {
		final Injector injector = Activator.getInjector();
		source = injector.getInstance(CollaborationsViewContentProvider.class);
		socket = injector.getInstance(CahootsRealtimeClient.class);
		connection = injector.getInstance(ConnectionDetails.class);
		sharedDocumentManager = injector
				.getInstance(ShareDocumentManager.class);
	}

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(source);
		viewer.setLabelProvider(new CollaboratorsViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		source.addContentChangedListener(listener);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(viewer.getControl(), "cahoots-eclipse.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	@Override
	public void dispose() {
		source.removeContentChangedListener(listener);
		super.dispose();
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				CollaborationsView.this.fillContextMenu(manager);
			}
		});
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(final IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(final IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			@Override
			public void run() {
				final ISelection selection = viewer.getSelection();
				final Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				final Collaboration collab = (Collaboration) obj;
				socket.send(new LeaveCollaborationMessage(connection
						.getUsername(), collab.getOpId()));
				sharedDocumentManager.removeDocumentListner(collab.getOpId());
			}
		};
		action1.setText("Leave");
		action1.setToolTipText("Leave the collaboration");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			@Override
			public void run() {
				final ISelection selection = viewer.getSelection();
				final Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				final Collaboration collab = (Collaboration) obj;
				final InviteDocumentDialog dia = new InviteDocumentDialog(
						CollaborationsView.this.getSite().getWorkbenchWindow()
								.getShell(), collab.getOpId());
				dia.open();
			}
		};
		action2.setText("Invite");
		action2.setToolTipText("Invite a new user to the collaboration");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			@Override
			public void run() {
				final ISelection selection = viewer.getSelection();
				final Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				final Collaboration collab = (Collaboration) obj;
				Activator.getInjector().getInstance(Chat.class)
						.startChat(collab.getCollaborators().get(0));
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(final DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}