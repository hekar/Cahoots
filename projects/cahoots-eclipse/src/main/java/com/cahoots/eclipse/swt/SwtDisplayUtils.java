package com.cahoots.eclipse.swt;

import org.eclipse.swt.widgets.Display;

public class SwtDisplayUtils {

	/**
	 * TODO: Should this be static? What about mocking?
	 */
	public static void async(final Runnable runnable) {
		Display.getDefault().asyncExec(runnable);
	}

	public static void sync(final Runnable runnable) {
		Display.getDefault().syncExec(runnable);
	}
}
