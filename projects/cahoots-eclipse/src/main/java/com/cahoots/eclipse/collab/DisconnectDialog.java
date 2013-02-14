package com.cahoots.eclipse.collab;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.google.inject.Injector;

public class DisconnectDialog extends Dialog {

	private Object result;
	private Shell shell;
	private CahootsConnection cahootsConnection;
	private CahootsSocket cahootsSocket;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public DisconnectDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");

		Injector injector = Activator.getInjector();
		cahootsConnection = injector.getInstance(CahootsConnection.class);
		cahootsSocket = injector.getInstance(CahootsSocket.class);
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(262, 91);
		shell.setText(getText());

		// Disconnect label
		Label disconnectLabel = new Label(shell, SWT.NONE);
		disconnectLabel.setBounds(10, 10, 244, 15);
		disconnectLabel.setText("Would you like to disconnect from Cahoots?");

		// Disconnect button
		Button disconnect = new Button(shell, SWT.NONE);
		disconnect.setBounds(98, 31, 75, 25);
		disconnect.setText("Disconnect");
		disconnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HttpClient client = new HttpClient();
				PostMethod method = new PostMethod("http://"
						+ cahootsConnection.getServer() + "/app/logout");
				List<NameValuePair> data = new LinkedList<NameValuePair>();
				data.add(new NameValuePair("auth_token", cahootsConnection
						.getAuthToken()));

				method.setRequestBody(data.toArray(new NameValuePair[data
						.size()]));
				try {
					int statusCode = client.executeMethod(method);
					if (statusCode == 200) {
						cahootsConnection
								.updateConnectionDetails(new ConnectionDetails(
										"", "", "", ""));
						cahootsSocket.disconnect();
						shell.close();
					} else {
						MessageDialog.openInformation(null, "Disconnect Error",
								"Error disconnecting from server");
					}

				} catch (Exception ex) {
					MessageDialog.openInformation(
							null,
							"Disconnect Error",
							"Error disconnecting from server. "
									+ ex.getMessage());
				}
			}
		});

		// Cancel button
		Button cancel = new Button(shell, SWT.NONE);
		cancel.setBounds(179, 31, 75, 25);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}
}
