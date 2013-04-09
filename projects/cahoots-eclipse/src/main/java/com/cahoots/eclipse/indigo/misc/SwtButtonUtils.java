package com.cahoots.eclipse.indigo.misc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

public class SwtButtonUtils {
	public static void doClick(final Button button) {
		button.notifyListeners(SWT.Selection, null);
	}

	public static void doMouseDown(final Button button) {
		button.notifyListeners(SWT.MouseDown, null);
	}

}
