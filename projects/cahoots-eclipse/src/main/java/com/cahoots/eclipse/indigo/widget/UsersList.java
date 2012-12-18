package com.cahoots.eclipse.indigo.widget;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.miginfocom.swt.MigLayout;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.cahoots.eclipse.Activator;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;
import com.cahoots.events.UserChangeEventListener;
import com.cahoots.http.CahootsHttpService;
import com.cahoots.json.Collaborator;
import com.cahoots.json.receive.UserChangeMessage;
import com.cahoots.websocket.CahootsSocket;

public class UsersList extends Composite implements UserChangeEventListener,
		DisconnectEventListener {
	private TableViewer viewer;
	private ViewContentProvider source;

	class ViewContentProvider implements IStructuredContentProvider {
		Map<String, Collaborator> elements = new LinkedHashMap<String, Collaborator>();

		public ViewContentProvider()
		{
			super();
		}

		public void inputChanged(final Viewer v, final Object oldInput, final Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(final Object parent) {
			return elements.values().toArray(new Object[elements.size()]);
		}

		public void add(final Collaborator element)
		{
			elements.put(element.username, element);
		}

		public void clear()
		{
			elements.clear();
		}

	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(final Object obj, final int index) {
			return getText(obj);
		}

		public Image getColumnImage(final Object obj, final int index) {
			return getImage(obj);
		}

		public Image getImage(final Object obj) {
			if (obj instanceof Collaborator)
			{
				final Collaborator c = (Collaborator) obj;
				if ("online".equals(c.status))
				{
					return PlatformUI.getWorkbench().
							getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
				}
				else
				{
					return PlatformUI.getWorkbench().
							getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVE);
				}
			}
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR);

		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public UsersList(final Composite parent, final int style) {
		super(parent, style);

		setLayout(new MigLayout("fill"));
		
		viewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getControl().setLayoutData("grow");
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		source = new ViewContentProvider();
		viewer.setContentProvider(source);
		
		populateUsersList();
		
		final CahootsSocket socket = CahootsSocket.getInstance();
		socket.addUserLoginEventListener(this);
		socket.addDisconnectEventListener(this);
	}

	private void populateUsersList() {
		final Job job = new Job("Job1") {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				final CahootsHttpService service = new CahootsHttpService(Activator.getServer());

				final List<String> users = service.listUsers().getUsers();

				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						source.clear();
						for (final String user : users) {
							source.add(new Collaborator(user));
						}
					}
				});

				return Status.OK_STATUS;
			}
		};

		job.schedule();
	}
	
	@Override
	public void userDisconnected(final DisconnectEvent event) {
		// TODO: What to do here?
	}

	@Override
	public void onEvent(UserChangeMessage msg) {
		source.add(msg.user);
		
	}
}
