package com.cahoots.eclipse.collab;


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

public class ConnectDialog extends Dialog {

	protected Object result;
	public Shell shlConnectToCahoots;
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
				try {
					String username = tb_username.getText();
					String password = tb_password.getText();
					String server = cb_server.getText();
					Activator.connect(username, password, server);
					shlConnectToCahoots.close();
				} catch (Exception e1) {
					e1.printStackTrace();
					MessageDialog.openInformation(
							null,
							"Connect Error",
							"Error connecting to server");
				}
			}
		});
		btn_connect.setBounds(278, 87, 75, 25);
		btn_connect.setText("Connect");

	}
}
