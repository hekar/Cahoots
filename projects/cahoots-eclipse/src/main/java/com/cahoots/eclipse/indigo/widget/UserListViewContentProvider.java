package com.cahoots.eclipse.indigo.widget;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.internal.dialogs.ViewContentProvider;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.swt.SwtDisplayUtils;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;
import com.cahoots.events.UserChangeEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.receive.UserChangeMessage;

@SuppressWarnings("restriction")
public class UserListViewContentProvider extends ViewContentProvider {

	private Map<String, Collaborator> elements = new ConcurrentHashMap<String, Collaborator>();
	private List<SourceContentChangedListener> listeners = new ArrayList<SourceContentChangedListener>();
	private final CahootsConnection cahootsConnection;

	@Inject
	public UserListViewContentProvider(final Activator activator,
			final CahootsSocket cahootsServer,
			final CahootsConnection cahootsConnection) {
		this.cahootsConnection = cahootsConnection;

		cahootsServer.addUserLoginEventListener(new UserChangeEventListener() {
			@Override
			public void onEvent(final UserChangeMessage msg) {
				add(msg.getUser());
				SwtDisplayUtils.sync(new Runnable() {
					@Override
					public void run() {
						fireContentChangedListeners();
					}
				});
			}
		});

		cahootsServer.addDisconnectEventListener(new DisconnectEventListener() {
			@Override
			public void onEvent(final DisconnectEvent msg) {
				clear();
				SwtDisplayUtils.sync(new Runnable() {
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
		final Collection<Collaborator> elements = this.elements.values();

		// Do not return the currently logged in username
		return filter(
				having(on(Collaborator.class).getUsername(),
						not(equalTo(cahootsConnection.getUsername()))),
				elements).toArray();
	}

	public void add(final Collaborator element) {
		elements.put(element.getUsername(), element);
	}

	public void clear() {
		elements.clear();
	}

	public void addContentChangedListener(
			final SourceContentChangedListener listener) {
		listeners.add(listener);
	}

	public void removeContentChangedListener(
			final SourceContentChangedListener listener) {
		listeners.remove(listener);
	}

	private void fireContentChangedListeners() {
		for (final SourceContentChangedListener listener : listeners) {
			listener.onEvent(this);
		}
	}

}