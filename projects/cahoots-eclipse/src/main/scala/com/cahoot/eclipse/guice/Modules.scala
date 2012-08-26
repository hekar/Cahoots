package com.cahoot.eclipse.guice

import com.google.inject._
import com.cahoot.eclipse.collab._
import com.cahoot.eclipse.indigo.editor._
import com.cahoot.client.connection._

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
