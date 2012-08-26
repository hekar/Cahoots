package com.cahoot.eclipse.collab

import org.eclipse.jface.viewers.IContentProvider
import org.eclipse.jface.viewers.Viewer
import org.eclipse.jface.viewers.IStructuredContentProvider

class CollaboratorSelectionContentProvider extends IStructuredContentProvider {

  def dispose(): Unit = {}

  def inputChanged(viewer: Viewer, oldInput: Object, newInput: Object): Unit = {
  }

  def getElements(element: Object): Array[java.lang.Object] = {
    Array("A", "B", "C")
  } 
}