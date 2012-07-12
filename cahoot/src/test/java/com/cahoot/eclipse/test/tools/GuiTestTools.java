package com.cahoot.eclipse.test.tools;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class GuiTestTools {
	public static void blockTillQuit(ApplicationWindow dialog) {
		
		Shell shell = dialog.getShell();
		if (!shell.isVisible())
		{
			dialog.open();
		}
		
		Display display = shell.getDisplay();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}
		display.dispose();
		
//		ApplicationWindow dialog = new ConnectionSelectionDialog();
//		GuiTestTools.blockTillQuit(dialog);
//		ApplicationWindow dialog = new ManageConnectionsDialog();
//		GuiTestTools.blockTillQuit(dialog);
	}
}
