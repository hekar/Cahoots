package com.cahoot.eclipse.chat

import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.jface.viewers.Viewer
import com.cahoot.client.services.ChatService
import com.cahoot.client.services.CollabService
import com.google.inject.Inject

@Inject
class ChatContentProvider(
  val collabService: CollabService,
  val chatService: ChatService)
  extends IStructuredContentProvider {

  def getElements(inputElement: Object): Array[Object] = {
    Array(collabService.listCollaborators())
  }

  def dispose(): Unit = {}

  def inputChanged(viewer: Viewer, oldInput: Object, newInput: Object): Unit = {}

}