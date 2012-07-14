package com.cahoot.eclipse.test.tools
import org.eclipse.jface.window.ApplicationWindow
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell

object SwtTestTools {
  def blockTillQuit(dialog: ApplicationWindow): Unit = {

    val shell = dialog.getShell();
    if (shell == null || !shell.isVisible()) {
      dialog.open();
    }

    if (shell != null) {
      val display = shell.getDisplay();
      while (!shell.isDisposed()) {
        if (!display.readAndDispatch()) {
          display.sleep();
        }
      }
      display.dispose();
    }
  }
}