package com.cahoots.eclipse.event;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.cahoots.eclipse.collab.share.ShareDocumentRegistrar;

/**
 * This class is a hacky, stupid piece of crap - me
 * 
 * @author Hekar
 * 
 */
public class EventRegistrarManager {

	private boolean registered = false;
	private List<EventRegistrar> eventRegistrars = new ArrayList<EventRegistrar>();

	@Inject
	public EventRegistrarManager(
			final ShareDocumentRegistrar shareDocumentRegistrar) {
		eventRegistrars.add(shareDocumentRegistrar);
	}

	public void registerAllEvents() {
		if (registered) {
			throw new IllegalStateException(
					"Events may not be registered twice");
		} else {
			for (final EventRegistrar registrar : eventRegistrars) {
				registrar.registerEvents();
			}

			registered = true;
		}
	}
}
