package com.cahoots.eclipse.indigo.widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.dialogs.ViewContentProvider;

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;
import com.cahoots.events.UserChangeEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.receive.UserChangeMessage;

@SuppressWarnings("restriction")
public class UserListViewContentProvider extends ViewContentProvider {

	private Map<String, Collaborator> elements = new LinkedHashMap<String, Collaborator>();
	private List<SourceContentChangedListener> listeners = new ArrayList<SourceContentChangedListener>();

	@Inject
	public UserListViewContentProvider(Activator activator,
			CahootsSocket cahootsServer) {
		final Display display = activator.getWorkbench().getDisplay();

		cahootsServer.addUserLoginEventListener(new UserChangeEventListener() {
			@Override
			public void onEvent(UserChangeMessage msg) {
				add(msg.user);
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						fireContentChangedListeners();
					}
				});
			}
		});

		cahootsServer.addDisconnectEventListener(new DisconnectEventListener() {
			@Override
			public void userDisconnected(DisconnectEvent event) {
				clear();
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						fireContentChangedListeners();
					}
				});
			}
		});
	}

	public void inputChanged(final Viewer v, final Object oldInput,
			final Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(final Object parent) {
		return elements.values().toArray();
	}

	public void add(final Collaborator element) {
		elements.put(element.getUsername(), element);
	}

	public void clear() {
		elements.clear();
	}

	public void addContentChangedListener(SourceContentChangedListener listener) {
		listeners.add(listener);
	}

	public void removeContentChangedListener(SourceContentChangedListener listener) {
		listeners.remove(listener);
	}

	private void fireContentChangedListeners() {
		for (SourceContentChangedListener listener : listeners) {
			listener.onEvent(this);
		}
	}

}