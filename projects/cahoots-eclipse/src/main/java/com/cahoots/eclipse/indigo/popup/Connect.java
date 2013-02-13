package com.cahoots.eclipse.indigo.popup;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.collab.ConnectDialog;
import com.cahoots.eclipse.indigo.widget.MessageDialog;
import com.cahoots.eclipse.indigo.widget.MessageDialogStatus;
import com.cahoots.events.ConnectEvent;
import com.cahoots.events.ConnectEventListener;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;

public class Connect implements IWorkbenchWindowActionDelegate {

	private Shell shell = null;
	private CahootsConnection connection;
	private CahootsSocket socket;
	private MessageDialog messageDialog;

	@Override
	public void init(final IWorkbenchWindow window) {
		connection = Activator.getInjector().getInstance(
				CahootsConnection.class);
		socket = Activator.getInjector().getInstance(CahootsSocket.class);
		messageDialog = Activator.getInjector()
				.getInstance(MessageDialog.class);

		socket.addConnectEventListener(new ConnectEventListener() {
			@Override
			public void connected(final ConnectEvent event) {
			}
		});

		socket.addDisconnectEventListener(new DisconnectEventListener() {
			@Override
			public void onEvent(final DisconnectEvent msg) {
			}
		});

		shell = window.getShell();
	}

	@Override
	public void run(final IAction action) {
		final boolean connected = connection.isAuthenticated();

		if (connected) {
			final MessageDialogStatus prompt = messageDialog
					.prompt(shell, "Already Connected?",
							"Would you like to disconnect before connecting to another server?");

			if (prompt == MessageDialogStatus.OK) {

				HttpClient client = new HttpClient();
				PostMethod method = new PostMethod("http://"
						+ connection.getServer() + "/app/logout");
				List<NameValuePair> data = new LinkedList<NameValuePair>();
				data.add(new NameValuePair("auth_token", connection
						.getAuthToken()));

				method.setRequestBody(data.toArray(new NameValuePair[data
						.size()]));
				try {
					int statusCode = client.executeMethod(method);
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

				} catch (Exception ex) {
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

	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
	}

	@Override
	public void dispose() {
	}

}
