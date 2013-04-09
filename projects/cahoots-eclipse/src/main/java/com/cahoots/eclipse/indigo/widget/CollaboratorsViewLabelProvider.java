package com.cahoots.eclipse.indigo.widget;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.cahoots.serialize.json.Collaboration;
import com.google.common.base.Joiner;

public class CollaboratorsViewLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(final Object arg0, final int arg1) {
		return null;
	}

	@Override
	public String getColumnText(final Object arg0, final int arg1) {
		final Collaboration collab = (Collaboration) arg0;
		return collab.getDocumentId() + " - "
				+ Joiner.on(", ").join(collab.getCollaborators());
	}

}
