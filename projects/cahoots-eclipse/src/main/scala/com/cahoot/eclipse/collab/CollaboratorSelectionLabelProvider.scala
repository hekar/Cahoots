package com.cahoot.eclipse.collab

import org.eclipse.jface.viewers.ILabelProvider
import org.eclipse.jface.viewers.ILabelProviderListener
import org.eclipse.swt.graphics.Image

class CollaboratorSelectionLabelProvider extends ILabelProvider {

  def getImage(element: Object): Image = { null }

  def getText(element: Object): String = { element.toString() }

  def addListener(listener: ILabelProviderListener): Unit = {}

  def dispose(): Unit = {}

  def isLabelProperty(element: Object, property: String): Boolean = { false }

  def removeListener(listener: ILabelProviderListener): Unit = {}

}