package com.cahoot.eclipse.guice
import com.cahoot.eclipse.collab.CollaborationShareStarter
import com.cahoot.eclipse.collab.CollaboratorSelectionContentProvider
import com.cahoot.eclipse.collab.CollaboratorSelectionLabelProvider
import com.cahoot.eclipse.editor.EditorHook
import com.google.inject.AbstractModule
import com.cahoot.client.connection.ConnectionStore
import com.cahoot.client.connection.Connection
import com.google.inject.Provider

class GuiModule extends AbstractModule {
  def configure(): Unit = {
    bind(classOf[EditorHook])
    bind(classOf[CollaborationShareStarter])
    bind(classOf[EditorHook])
    bind(classOf[CollaboratorSelectionContentProvider])
    bind(classOf[CollaboratorSelectionLabelProvider])

    bind(classOf[Connection]).toProvider(classOf[ConnectionProvider])

    bind(classOf[ConnectionStore]).toInstance(new ConnectionStore)
  }
}
