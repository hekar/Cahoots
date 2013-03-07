package com.cahoots.eclipse.indigo.popup;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.collab.ConnectDialog;
import com.cahoots.eclipse.collab.DisconnectDialog;
import com.cahoots.eclipse.indigo.widget.MessageDialog;
import com.cahoots.eclipse.indigo.widget.MessageDialogStatus;
import com.google.inject.Inject;

public class ConnectStuff {

	CahootsConnection connection;
	Shell shell;
	MessageDialog messageDialog;
	CahootsSocket socket;

	@Inject
	public ConnectStuff(final CahootsConnection details,
			final MessageDialog dialog, final CahootsSocket socket) {
		this.connection = details;
		this.messageDialog = dialog;
		this.socket = socket;

		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	@SuppressWarnings("deprecation")
	public void connect() {
		final boolean connected = connection.isAuthenticated();

		if (connected) {
			final MessageDialogStatus prompt = messageDialog
					.prompt(shell, "Already Connected?",
							"Would you like to disconnect before connecting to another server?");

			if (prompt == MessageDialogStatus.OK) {

				final HttpClient client = new HttpClient();
				final PostMethod method = new PostMethod("http://"
						+ connection.getServer() + "/app/logout");
				final List<NameValuePair> data = new LinkedList<NameValuePair>();
				data.add(new NameValuePair("auth_token", connection
						.getAuthToken()));

				method.setRequestBody(data.toArray(new NameValuePair[data
						.size()]));
				try {
					final int statusCode = client.executeMethod(method);
					if (statusCode == 200) {
						connection
								.updateConnectionDetails(new ConnectionDetails(
										"", "", "", ""));
						socket.disconnect();
					} else {
						org.eclipse.jface.dialogs.MessageDialog
								.openInformation(null, "Disconnect Error",
										"Error disconnecting from server");
					}

				} catch (final Exception ex) {
					org.eclipse.jface.dialogs.MessageDialog.openInformation(
							null,
							"Disconnect Error",
							"Error disconnecting from server. "
									+ ex.getMessage());
				}
			}
		}

		final ConnectDialog dialog = new ConnectDialog(shell);
		dialog.open();
	}

	@SuppressWarnings("deprecation")
	public void disconnect() {
		final boolean authenticated = connection.isAuthenticated();
		if (authenticated) {
			final DisconnectDialog dialog = new DisconnectDialog(shell,
					SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialog.open();
		}

	}
}
