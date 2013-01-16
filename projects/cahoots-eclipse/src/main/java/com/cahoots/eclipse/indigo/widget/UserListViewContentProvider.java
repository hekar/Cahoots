package com.cahoots.eclipse.indigo.widget;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.cahoots.json.Collaborator;

class UserListViewContentProvider implements IStructuredContentProvider {
	Map<String, Collaborator> elements = new LinkedHashMap<String, Collaborator>();

	public UserListViewContentProvider() {
		super();
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
		elements.put(element.username, element);
	}

	public void clear() {
		elements.clear();
	}

}