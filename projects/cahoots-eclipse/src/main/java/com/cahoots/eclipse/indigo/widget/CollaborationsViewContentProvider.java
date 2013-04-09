package com.cahoots.eclipse.indigo.widget;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.cahoots.eclipse.collab.share.ShareDocumentManager;
import com.cahoots.events.CollaboratorJoinedEventListener;
import com.cahoots.events.CollaboratorLeftEventListener;
import com.cahoots.events.CollaboratorsListEventListener;
import com.cahoots.serialize.json.Collaboration;
import com.cahoots.serialize.json.receive.CollaboratorJoinedMessage;
import com.cahoots.serialize.json.receive.CollaboratorLeftMessage;
import com.cahoots.serialize.json.receive.CollaboratorsListMessage;

@SuppressWarnings("restriction")
public class CollaborationsViewContentProvider extends ViewContentProvider {

	private final Map<String, Collaboration> elements = new ConcurrentHashMap<String, Collaboration>();
	private final List<SourceContentChangedListener> listeners = new ArrayList<SourceContentChangedListener>();

	@Inject
	public CollaborationsViewContentProvider(final Activator activator,
			final CahootsSocket cahootsServer,
			final ShareDocumentManager shareManager,
			final CahootsConnection connection) {

		cahootsServer
				.addCollaboratorsListEventListener(new CollaboratorsListEventListener() {

					@Override
					public void onEvent(final CollaboratorsListMessage msg) {
						elements.put(
								msg.getOpId(),
								new Collaboration(msg.getOpId(), shareManager
										.getDocumentId(msg.getOpId()), msg
										.getCollaborators()));
						SwtDisplayUtils.sync(new Runnable() {
							@Override
							public void run() {
								fireContentChangedListeners();
							}
						});
					}
				});
		cahootsServer
				.addCollaboratorJoinedEventListener(new CollaboratorJoinedEventListener() {

					@Override
					public void onEvent(final CollaboratorJoinedMessage msg) {
						if (elements.containsKey(msg.getOpId())) {
							elements.get(msg.getOpId()).getCollaborators()
									.add(msg.getUser());
						} else {
							elements.put(
									msg.getOpId(),
									new Collaboration(msg.getOpId(),
											shareManager.getDocumentId(msg
													.getOpId()), Arrays
													.asList(new String[] { msg
															.getUser() })));
						}
						SwtDisplayUtils.sync(new Runnable() {
							@Override
							public void run() {
								fireContentChangedListeners();
							}
						});
					}
				});
		cahootsServer
				.addCollaboratorLeftEventLisener(new CollaboratorLeftEventListener() {

					@Override
					public void onEvent(final CollaboratorLeftMessage msg) {

						if (elements.containsKey(msg.getOpId())) {
							if (connection.isLoggedInUser(msg.getUser())) {
								elements.remove(msg.getOpId());
								shareManager.removeDocumentListner(msg
										.getOpId());
							} else {
								elements.get(msg.getOpId()).getCollaborators()
										.remove(msg.getUser());
								if (elements.get(msg.getOpId())
										.getCollaborators().size() == 0) {
									elements.remove(msg.getOpId());
									shareManager.removeDocumentListner(msg
											.getOpId());
								}
							}
							SwtDisplayUtils.sync(new Runnable() {
								@Override
								public void run() {
									fireContentChangedListeners();
								}
							});
						}
					}
				});
	}

	@Override
	public void inputChanged(final Viewer v, final Object oldInput,
			final Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(final Object parent) {
		final Collection<Collaboration> elements = this.elements.values();

		return elements.toArray();
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

	public Collaboration get(final String opId) {
		return elements.get(opId);
	}

}
