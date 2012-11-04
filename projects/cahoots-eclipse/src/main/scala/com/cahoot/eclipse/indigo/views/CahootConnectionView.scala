package com.cahoot.eclipse.indigo.views

import java.lang.Override
import java.util.Comparator

import org.eclipse.jface.viewers.ITableLabelProvider
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.ArrayContentProvider
import org.eclipse.jface.viewers.ILabelProviderListener
import org.eclipse.swt.SWT

import org.eclipse.jface.viewers.LabelProvider
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.part.ViewPart
import org.eclipse.swt.graphics.Image

private class CahootConnectionComparator extends Comparator[String] {
  def compare(x1: String, x2: String): Int = x1.compareTo(x2)
}

class CahootConnectionView extends ViewPart {

	private var viewer: TableViewer = _

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	def createPartControl(parent: Composite): Unit = {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//DeferredContentProvider cannot be used here
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider());
		
		viewer.setInput(getViewSite());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	override def setFocus(): Unit = {
		viewer.getControl().setFocus();
	}
	
}