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

import com.cahoots.eclipse.Activator;
import com.cahoots.websocket.CahootsSocket;

public class DisconnectDialog extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DisconnectDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
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
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 244, 15);
		lblNewLabel.setText("Would you like to disconnect from Cahoots?");
		
		Button btn_disconnect = new Button(shell, SWT.NONE);
		btn_disconnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO move this stuff outside the UI thread
				
				HttpClient client = new HttpClient();
				PostMethod method = new PostMethod( Activator.getServer() + "/app/logout");
				List<NameValuePair> data = new LinkedList<NameValuePair>();
				data.add( new NameValuePair("auth_token", Activator.getAuthToken()));
				
				method.setRequestBody(data.toArray(new NameValuePair[data.size()]));
				try
				{
					int statusCode = client.executeMethod(method);
					if(statusCode == 200)
					{
						Activator.setAuthToken(null);
						CahootsSocket.getInstance().disconnect();
						shell.close();
					}
					else
					{
					    MessageDialog.openInformation(
								null,
								"Disconnect Error",
								"Error disconnecting from server");
					}
					
				}
				catch(Exception ex)
				{
					MessageDialog.openInformation(
							null,
							"Disconnect Error",
							"Error disconnecting from server. " + ex.getMessage());
				}
			}
		});
		btn_disconnect.setBounds(98, 31, 75, 25);
		btn_disconnect.setText("Disconnect");
		
		Button btn_cancel = new Button(shell, SWT.NONE);
		btn_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btn_cancel.setBounds(179, 31, 75, 25);
		btn_cancel.setText("Cancel");

	}

}
