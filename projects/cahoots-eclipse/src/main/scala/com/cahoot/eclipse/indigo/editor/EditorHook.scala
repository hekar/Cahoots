package com.cahoot.eclipse.indigo.editor
import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.ui.IEditorReference
import org.eclipse.ui.PlatformUI
import scala.collection.mutable.HashSet

/**
 * Static functions for the EditorHook
 */
object EditorHook {
  def getActiveTextEditor(): ITextEditor = {
    val workbench = PlatformUI.getWorkbench()
    if (workbench == null) {
      return null
    }
    val ww = workbench.getActiveWorkbenchWindow()
    if (ww == null) {
      return null
    }

    val wp = ww.getActivePage()
    if (wp == null) {
      return null
    }

    val ep = wp.getActiveEditor()
    if (ep.isInstanceOf[ITextEditor]) {
      return ep.asInstanceOf[ITextEditor]
    }

    if (ep != null) {
      return ep.getAdapter(classOf[ITextEditor]).asInstanceOf[ITextEditor]
    }

    return null
  }
}

/**
 * Entry point to bootstrap and hook editors into event listeners
 */
class EditorHook {

  val editors = new HashSet[IEditorReference]

  def isEditorHooked(editor: IEditorReference): Boolean = {
    editors.contains(editor)
  }

  def hookEditor(editor: IEditorReference): Unit = {
    editors += editor
  }

}