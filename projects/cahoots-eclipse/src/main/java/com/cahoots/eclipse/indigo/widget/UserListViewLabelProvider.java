package com.cahoots.eclipse.indigo.widget;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.cahoots.json.Collaborator;

public class UserListViewLabelProvider extends LabelProvider implements
		ITableLabelProvider {
	
	public UserListViewLabelProvider() {
	}
	
	public String getColumnText(final Object obj, final int index) {
		return getText(obj);
	}

	public Image getColumnImage(final Object obj, final int index) {
		return getImage(obj);
	}

	public Image getImage(final Object obj) {
		if (obj instanceof Collaborator) {
			final Collaborator collaborator = (Collaborator) obj;
			if ("online".equals(collaborator.getStatus())) {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_ELEMENT);
			} else {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_ELCL_REMOVE);
			}
		} else {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_DEC_FIELD_ERROR);
		}
	}
}