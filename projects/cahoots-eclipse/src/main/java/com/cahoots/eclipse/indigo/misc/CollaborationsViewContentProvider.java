package com.cahoots.eclipse.indigo.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.internal.dialogs.ViewContentProvider;

import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.serialize.Collaboration;
import com.cahoots.connection.serialize.receive.CollaboratorJoinedMessage;
import com.cahoots.connection.serialize.receive.CollaboratorLeftMessage;
import com.cahoots.connection.serialize.receive.CollaboratorsListMessage;
import com.cahoots.connection.websocket.CahootsRealtimeClient;
import com.cahoots.eclipse.collab.share.ShareDocumentManager;
import com.cahoots.event.CollaboratorJoinedEventListener;
import com.cahoots.event.CollaboratorLeftEventListener;
import com.cahoots.event.CollaboratorsListEventListener;

@SuppressWarnings("restriction")
public class CollaborationsViewContentProvider extends ViewContentProvider {

	private final Map<String, Collaboration> elements = new ConcurrentHashMap<String, Collaboration>();
	private final List<SourceContentChangedListener> listeners = new ArrayList<SourceContentChangedListener>();

	private final ShareDocumentManager shareManager;

	@Inject
	public CollaborationsViewContentProvider(
			final CahootsRealtimeClient cahootsServer,
			final ShareDocumentManager shareManager,
			final ConnectionDetails connection) {

		this.shareManager = shareManager;

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
		for (final String opId : elements.keySet()) {
			shareManager.removeDocumentListner(opId);
		}
		elements.clear();

		SwtDisplayUtils.sync(new Runnable() {
			@Override
			public void run() {
				fireContentChangedListeners();
			}
		});
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
