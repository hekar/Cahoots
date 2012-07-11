package com.cahoot.eclipse.chat

import org.eclipse.jface.viewers.LabelProvider
import org.eclipse.jface.viewers.ILabelProviderListener

class ChatLabelProvider extends LabelProvider {

  override def addListener(listener: ILabelProviderListener): Unit = {}
  
  override def dispose(): Unit = {}
  
  override def isLabelProperty(element: Object, property: String): Boolean = { false }
  
  override def removeListener(listener: ILabelProviderListener): Unit = {}

}