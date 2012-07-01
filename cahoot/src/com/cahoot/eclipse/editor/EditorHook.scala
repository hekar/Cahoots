package com.cahoot.eclipse.editor
import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.ui.IEditorReference
import org.eclipse.ui.PlatformUI

import com.cahoot.eclipse.editor.IEditorHook

/**
 * Static functions for the EditorHook
 */
object EditorHook {
  	def getTextEditor(): ITextEditor = {
		val workbench = PlatformUI.getWorkbench()
		if (workbench == null)
		{
			return null
		}
		val ww = workbench.getActiveWorkbenchWindow()
		if (ww == null)
		{
			return null
		}
		
		val wp = ww.getActivePage()
		if (wp == null)
		{
			return null
		}
		
		val ep = wp.getActiveEditor()
		if (ep.isInstanceOf[ITextEditor])
		{
			return ep.asInstanceOf[ITextEditor]
		}
		
		if (ep != null)
		{
			return ep.getAdapter(classOf[ITextEditor]).asInstanceOf[ITextEditor]
		}
		
		return null
	}
}

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
    
    if (page != null) {
	    
    }
  }
  
}