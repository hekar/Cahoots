package com.cahoot.eclipse.connection

import org.eclipse.jface.viewers.ILabelProviderListener
import org.eclipse.jface.viewers.LabelProvider

class CollaboratorModificationLabelProvider extends LabelProvider {

  override def addListener(listener: ILabelProviderListener): Unit = {}
  override def dispose(): Unit = {}
  override def isLabelProperty(element: Object, property: String): Boolean = { false }
  override def removeListener(listener: ILabelProviderListener): Unit = {}

}