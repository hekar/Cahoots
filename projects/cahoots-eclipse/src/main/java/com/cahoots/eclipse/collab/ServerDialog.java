package com.cahoots.eclipse.collab;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ServerDialog extends Window {

	String serverName = null;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ServerDialog(final Shell parent) {
		super(parent);

		setShellStyle(getShellStyle() | SWT.APPLICATION_MODAL);
	}

	/**
	 * Create contents of the dialog.
	 */
	@Override
	protected Composite createContents(final Composite parent) {
		final Shell shell = getShell();
		shell.setText("Connect to Cahoots");
		shell.setSize(420, 160);
		shell.setLayout(new MigLayout("fill"));

		final Composite content = parent;
		content.setLayout(new MigLayout("fill",
				"[growprio 0][growprio 100, fill]"));
		content.setLayoutData("grow");

		// Create the controls

		final Label server = new Label(content, SWT.NONE);
		server.setText("server:");

		final Text serverText = new Text(content, SWT.BORDER);
		serverText.setLayoutData("spanx, growx, wrap");

		final Button connect = new Button(content, SWT.NONE);
		connect.setText("&Add");
		connect.setLayoutData("skip 1, split 2, tag ok");

		connect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setReturnCode(SWT.OK);
				serverName = serverText.getText();
				close();
			}
		});

		final Button cancel = new Button(content, SWT.NONE);
		cancel.setText("&Cancel");
		cancel.setLayoutData("tag cancel");

		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setReturnCode(SWT.CANCEL);
				close();
			}
		});

		return content;
	}

	public String getServer() {
		if (serverName != null) {
			return serverName;
		}
		return null;
	}
}
