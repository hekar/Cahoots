package com.cahoots.eclipse.indigo.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MessageDialog {
	public void error(final Shell parent, final String title,
			final String message) {
		final MessageBox messageBox = new MessageBox(parent, SWT.ICON_ERROR
				| SWT.OK);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}

	public void warn(final Shell parent, final String title,
			final String message) {
		final MessageBox messageBox = new MessageBox(parent, SWT.ICON_WARNING
				| SWT.OK);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}

	public void info(final Shell parent, final String title,
			final String message) {
		final MessageBox messageBox = new MessageBox(parent,
				SWT.ICON_INFORMATION | SWT.OK);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}

	/**
	 * Returns {@link MessageDialogStatus}.OK on successful prompt. Cancel
	 * otherwise
	 */
	public MessageDialogStatus prompt(final Shell parent, final String title,
			final String question) {
		final MessageBox messageBox = new MessageBox(parent, SWT.ICON_QUESTION
				| SWT.OK | SWT.CANCEL);
		messageBox.setText(title);
		messageBox.setMessage(question);
		final int status = messageBox.open();
		switch (status) {
		case SWT.OK:
			return MessageDialogStatus.OK;
		default:
			return MessageDialogStatus.CANCEL;
		}
	}
}
