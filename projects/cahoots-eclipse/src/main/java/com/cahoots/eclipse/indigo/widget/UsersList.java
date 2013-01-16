package com.cahoots.eclipse.indigo.widget;

import java.util.List;

import net.miginfocom.swt.MigLayout;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.http.CahootsHttpService;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;
import com.cahoots.events.UserChangeEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.receive.UserChangeMessage;
import com.google.inject.Injector;

public class UsersList extends Composite {
	private TableViewer viewer;
	private UserListViewContentProvider source;
	private CahootsConnection cahootsConnection;
	private CahootsSocket cahootsSocket;

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public UsersList(final Composite parent, final int style) {
		super(parent, style);
		
		Injector injector = Activator.getInjector();
		cahootsConnection = injector.getInstance(CahootsConnection.class);
		cahootsSocket = injector.getInstance(CahootsSocket.class);

		setLayout(new MigLayout("fill"));

		viewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
		source = new UserListViewContentProvider();
		viewer.setContentProvider(source);
		viewer.setLabelProvider(new UserListViewLabelProvider());
		viewer.setSorter(new NameSorter());
		
		viewer.getControl().setLayoutData("grow");

		populateUsersList();

		cahootsSocket.addUserLoginEventListener(new UserChangeEventListener() {
			@Override
			public void onEvent(final UserChangeMessage msg) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						source.add(msg.user);
						viewer.refresh();
					}
				});
			}
		});

		cahootsSocket.addDisconnectEventListener(new DisconnectEventListener() {
			@Override
			public void userDisconnected(final DisconnectEvent event) {
			}
		});
	}

	private void populateUsersList() {
		final Job job = new Job("PopulateUserListJob") {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				final CahootsHttpService service = new CahootsHttpService(cahootsConnection);

				final List<String> users = service.listUsers().getUsers();

				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						source.clear();
						for (final String user : users) {
							source.add(new Collaborator(user));
						}
						viewer.refresh();
					}
				});

				return Status.OK_STATUS;
			}
		};

		job.schedule();
	}
}
