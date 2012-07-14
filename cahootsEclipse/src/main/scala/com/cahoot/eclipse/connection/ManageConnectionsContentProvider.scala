package com.cahoot.eclipse.connection

import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.jface.viewers.Viewer

class CollaboratorModificationContentProvider extends IStructuredContentProvider {

  def getElements(inputElement: Object): Array[Object] = { null }

  def dispose(): Unit = {}

  def inputChanged(viewer: Viewer, oldInput: Object, newInput: Object): Unit = {}

}