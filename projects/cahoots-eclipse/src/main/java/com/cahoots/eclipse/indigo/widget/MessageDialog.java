package com.cahoots.eclipse.indigo.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MessageDialog {
	public void error(Shell parent, String title, String message) {
		MessageBox messageBox = new MessageBox(parent, SWT.ICON_ERROR | SWT.OK);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}
	
	public void warn(Shell parent, String title, String message) {
		MessageBox messageBox = new MessageBox(parent, SWT.ICON_WARNING | SWT.OK);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}
	
	public void info(Shell parent, String title, String message) {
		MessageBox messageBox = new MessageBox(parent, SWT.ICON_INFORMATION | SWT.OK);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}
	
	/**
	 * Returns {@link MessageDialogStatus}.OK on successful prompt
	 */
	public MessageDialogStatus prompt(Shell parent, String title, String question) {
		MessageBox messageBox = new MessageBox(parent, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		messageBox.setText(title);
		messageBox.setMessage(question);
		int status = messageBox.open();
		switch (status) {
		case SWT.OK:
			return MessageDialogStatus.OK;
		default:
			return MessageDialogStatus.CANCEL;
		}
	}
}
