package com.cahoots.eclipse.collab;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cahoots.eclipse.Activator;
import com.cahoots.websocket.CahootsSocket;

public class ConnectDialog extends Dialog {

	protected Object result;
	protected Shell shlConnectToCahoots;
	private Text tb_username;
	private Text tb_password;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ConnectDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlConnectToCahoots.open();
		shlConnectToCahoots.layout();
		Display display = getParent().getDisplay();
		while (!shlConnectToCahoots.isDisposed()) {
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
		shlConnectToCahoots = new Shell(getParent(), getStyle());
		shlConnectToCahoots.setSize(450, 145);
		shlConnectToCahoots.setText("Connect to Cahoots");
		
		Label lblUsername = new Label(shlConnectToCahoots, SWT.NONE);
		lblUsername.setAlignment(SWT.RIGHT);
		lblUsername.setBounds(20, 7, 68, 15);
		lblUsername.setText("User Name:");
		
		Label lblNewLabel = new Label(shlConnectToCahoots, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(10, 34, 78, 15);
		lblNewLabel.setText("Password:");
		
		tb_username = new Text(shlConnectToCahoots, SWT.BORDER);
		tb_username.setBounds(94, 4, 259, 21);
		
		tb_password = new Text(shlConnectToCahoots, SWT.BORDER | SWT.PASSWORD);
		tb_password.setBounds(94, 31, 259, 21);
		
		Label lblNewLabel_1 = new Label(shlConnectToCahoots, SWT.NONE);
		lblNewLabel_1.setAlignment(SWT.RIGHT);
		lblNewLabel_1.setBounds(33, 55, 55, 15);
		lblNewLabel_1.setText("Server:");
		
		final Combo cb_server = new Combo(shlConnectToCahoots, SWT.NONE);
		cb_server.setItems(new String[] {"localhost:9000"});
		cb_server.setBounds(94, 58, 259, 23);
		cb_server.select(0);
		
		Button btn_server_edit = new Button(shlConnectToCahoots, SWT.NONE);
		btn_server_edit.setBounds(359, 56, 40, 25);
		btn_server_edit.setText("...");
		
		Button btn_cancel = new Button(shlConnectToCahoots, SWT.NONE);
		btn_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlConnectToCahoots.close();
			}
		});
		btn_cancel.setBounds(359, 87, 75, 25);
		btn_cancel.setText("Cancel");
		
		Button btn_connect = new Button(shlConnectToCahoots, SWT.NONE);
		btn_connect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO move this stuff outside the UI thread
				String username = tb_username.getText();
				String password = tb_password.getText();
				String server = cb_server.getText();
				
				HttpClient client = new HttpClient();
				PostMethod method = new PostMethod( "http://" + server + "/app/login");
				List<NameValuePair> data = new LinkedList<NameValuePair>();
				data.add( new NameValuePair("username", username));
				data.add( new NameValuePair("password", password));
				
				method.setRequestBody(data.toArray(new NameValuePair[data.size()]));
				try
				{
					int statusCode = client.executeMethod(method);
					if(statusCode == 200)
					{
						String authToken = new String(method.getResponseBody());
						Activator.setAuthToken(authToken);
						Activator.setServer(server);

						CahootsSocket.getInstance().connect(server, authToken);
						
						shlConnectToCahoots.close();
					}
					else
					{
					    MessageDialog.openInformation(
								null,
								"Connect Error",
								"Error connecting to server");
					}
					
				}
				catch(Exception ex)
				{
					MessageDialog.openInformation(
							null,
							"Connect Error",
							"Error connecting to server. " + ex.getMessage());
				}
			}
		});
		btn_connect.setBounds(278, 87, 75, 25);
		btn_connect.setText("Connect");

	}
}
