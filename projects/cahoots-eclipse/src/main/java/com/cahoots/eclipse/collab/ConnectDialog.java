package com.cahoots.eclipse.collab;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.indigo.job.BackgroundJob;
import com.cahoots.eclipse.indigo.job.BackgroundJobScheduler;
import com.google.inject.Injector;

public class ConnectDialog extends Dialog {

	private Object result;
	private Shell dialog;
	private final CahootsSocket socket;
	private final BackgroundJobScheduler backgroundJobScheduler;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ConnectDialog(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("SWT Dialog");

		Injector injector = Activator.getInjector();
		socket = injector.getInstance(CahootsSocket.class);
		backgroundJobScheduler = injector
				.getInstance(BackgroundJobScheduler.class);
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		dialog.open();
		dialog.layout();
		Display display = getParent().getDisplay();
		while (!dialog.isDisposed()) {
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
		dialog = new Shell(getParent(), getStyle());
		dialog.setSize(450, 145);
		dialog.setText("Connect to Cahoots");

		Label usernameLabel = new Label(dialog, SWT.NONE);
		usernameLabel.setAlignment(SWT.RIGHT);
		usernameLabel.setBounds(20, 7, 68, 15);
		usernameLabel.setText("User Name:");

		Label passwordLabel = new Label(dialog, SWT.NONE);
		passwordLabel.setAlignment(SWT.RIGHT);
		passwordLabel.setBounds(10, 34, 78, 15);
		passwordLabel.setText("Password:");

		final Text usernameEdit = new Text(dialog, SWT.BORDER);
		usernameEdit.setBounds(94, 4, 259, 21);

		final Text passwordEdit = new Text(dialog, SWT.BORDER | SWT.PASSWORD);
		passwordEdit.setBounds(94, 31, 259, 21);

		Label serverLabel = new Label(dialog, SWT.NONE);
		serverLabel.setAlignment(SWT.RIGHT);
		serverLabel.setBounds(33, 55, 55, 15);
		serverLabel.setText("Server:");

		final Combo servers = new Combo(dialog, SWT.NONE);
		servers.setItems(new String[] { "localhost:9000" });
		servers.setBounds(94, 58, 259, 23);
		servers.select(0);

		Button serverEdit = new Button(dialog, SWT.NONE);
		serverEdit.setBounds(359, 56, 40, 25);
		serverEdit.setText("...");

		Button cancel = new Button(dialog, SWT.NONE);
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialog.close();
			}
		});
		cancel.setBounds(359, 87, 75, 25);
		cancel.setText("C&ancel");

		Button connect = new Button(dialog, SWT.NONE);
		connect.setBounds(278, 87, 75, 25);
		connect.setText("&Connect");
		connect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final BackgroundJob backgroundJob = new BackgroundJob() {
					@Override
					public IStatus run(final IProgressMonitor monitor) {
						monitor.beginTask("Connecting to Cahoots", 100);
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									String username = usernameEdit.getText();
									String password = passwordEdit.getText();
									String server = servers.getText();
									socket.connect(username, password, server);
									dialog.close();
								} catch (Exception e1) {
									e1.printStackTrace();
									MessageDialog.openInformation(null,
											"Connect Error",
											"Error connecting to server");
								}

							}
						});

						return Status.OK_STATUS;
					}
				};
				
				backgroundJobScheduler.schedule("Connect to Cahoots",
						backgroundJob);
			}
		});
	}
}
