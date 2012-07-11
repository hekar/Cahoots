package com.cahoot.eclipse.tests.manual;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import com.cahoot.eclipse.collab.ConnectionSelectionDialog;

public class ManageConnectionsGui {

	@Test
	public void test() {
		ApplicationWindow dialog = 
				new ConnectionSelectionDialog();
		dialog.open();
		Shell shell = dialog.getShell();
		Display display = shell.getDisplay();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}
		display.dispose();
	}
}
