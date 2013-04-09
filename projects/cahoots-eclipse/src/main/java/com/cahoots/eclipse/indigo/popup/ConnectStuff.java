package com.cahoots.eclipse.indigo.popup;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.ui.PlatformUI;

import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.websocket.CahootsRealtimeClient;
import com.cahoots.eclipse.collab.ConnectDialog;
import com.cahoots.eclipse.collab.DisconnectDialog;
import com.cahoots.eclipse.indigo.misc.MessageDialog;
import com.cahoots.eclipse.indigo.misc.MessageDialogStatus;
import com.google.inject.Inject;

public class ConnectStuff {

	private ConnectionDetails connectionDetails;
	private MessageDialog messageDialog;
	private CahootsRealtimeClient socket;

	@Inject
	public ConnectStuff(final ConnectionDetails connectionDetails,
			final MessageDialog dialog, final CahootsRealtimeClient socket) {
		this.connectionDetails = connectionDetails;
		this.messageDialog = dialog;
		this.socket = socket;
	}

	@SuppressWarnings("deprecation")
	public void connect() {
		final boolean connected = connectionDetails.isAuthenticated();

		if (connected) {
			final MessageDialogStatus prompt = messageDialog
					.prompt(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(),
							"Already Connected?",
							"Would you like to disconnect before connecting to another server?");

			if (prompt == MessageDialogStatus.OK) {

				final HttpClient client = new HttpClient();
				final PostMethod method = new PostMethod("http://"
						+ connectionDetails.getServer() + "/app/logout");
				final List<NameValuePair> data = new LinkedList<NameValuePair>();
				data.add(new NameValuePair("auth_token", connectionDetails
						.getAuthToken()));

				method.setRequestBody(data.toArray(new NameValuePair[data
						.size()]));
				try {
					final int statusCode = client.executeMethod(method);
					if (statusCode == 200) {
						connectionDetails.disconnect();
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

		final ConnectDialog dialog = new ConnectDialog(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.open();
	}

	@SuppressWarnings("deprecation")
	public void disconnect() {
		final boolean authenticated = connectionDetails.isAuthenticated();
		if (authenticated) {
			final DisconnectDialog dialog = new DisconnectDialog(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getShell());
			dialog.open();
		}

	}
}
