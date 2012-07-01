package com.cahoot.eclipse.editor
import org.eclipse.ui.IEditorReference
import org.eclipse.ui.PlatformUI
import com.cahoot.eclipse.editor.IEditorHook
import org.eclipse.ui.IWorkbenchListener

/**
 * Entry point to bootstrap and hook editors into event listeners
 */
class EditorHook extends IEditorHook {

  def isEditorHooked(editor: IEditorReference): Boolean = {
    false
  }

  def hookEditor(editor: IEditorReference): Unit = {
     
  }
  
  def test(): Unit = {
    val page = PlatformUI.getWorkbench()
      .getActiveWorkbenchWindow().getActivePage();
    PlatformUI.getWorkbench().
    if (page != null) {
	    val editorReferences = page.getEditorReferences();
	    
	    editorReferences foreach {
	    	println _
	    }
    }
  }
}