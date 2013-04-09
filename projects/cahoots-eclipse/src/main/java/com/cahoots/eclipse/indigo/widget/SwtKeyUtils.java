package com.cahoots.eclipse.indigo.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

public class SwtKeyUtils {
	public static boolean enterPressed(KeyEvent e) {
		return e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR;
	}

	public static boolean enterPressed(int keyCode) {
		return keyCode == SWT.CR || keyCode == SWT.KEYPAD_CR;
	}
}
