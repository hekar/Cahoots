package com.cahoot.eclipse.guice
import com.cahoot.eclipse.collab.CollaborationShareStarter
import com.cahoot.eclipse.collab.CollaboratorSelectionContentProvider
import com.cahoot.eclipse.collab.CollaboratorSelectionLabelProvider
import com.cahoot.eclipse.editor.EditorHook
import com.cahoot.eclipse.editor.IEditorHook
import com.google.inject.AbstractModule
import com.cahoot.client.connection.ConnectionStore
import com.cahoot.client.connection.Connection

class GuiModule extends AbstractModule {
  def configure(): Unit = {
		  bind(classOf[EditorHook])
		  bind(classOf[CollaborationShareStarter])
		  bind(classOf[IEditorHook]).to(classOf[EditorHook])
		  bind(classOf[CollaboratorSelectionContentProvider])
		  bind(classOf[CollaboratorSelectionLabelProvider])
		  
		  bind(classOf[ConnectionStore]).toInstance(new ConnectionStore)
		  //bind(classOf[Connection]).toProvider()
		  
  }
}
