package com.cahoots.eclipse.collab;

import net.miginfocom.swt.MigLayout;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.indigo.job.BackgroundJob;
import com.cahoots.eclipse.indigo.job.BackgroundJobScheduler;
import com.cahoots.eclipse.indigo.widget.MessageDialog;
import com.cahoots.eclipse.swt.SwtButtonUtils;
import com.cahoots.eclipse.swt.SwtDisplayUtils;
import com.cahoots.eclipse.swt.SwtKeyUtils;
import com.cahoots.preferences.PreferenceConstants;
import com.google.inject.Injector;

public class ConnectDialog extends Window {

	private final CahootsSocket socket;
	private final BackgroundJobScheduler backgroundJobScheduler;
	private final MessageDialog messageDialog;
	private Combo servers;
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ConnectDialog(final Shell parent) {
		super(parent);

		final Injector injector = Activator.getInjector();
		socket = injector.getInstance(CahootsSocket.class);
		backgroundJobScheduler = injector
				.getInstance(BackgroundJobScheduler.class);
		messageDialog = injector.getInstance(MessageDialog.class);
		Activator.getActivator().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				if(arg0.getProperty().equals(PreferenceConstants.P_SERVERS) && servers != null)
				{
					servers.removeAll();
					servers.setItems(((String)arg0.getNewValue()).split(","));
					servers.select(0);
				}
			}
		});
	}

	/**
	 * Create contents of the dialog.
	 */
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

		final Label usernameLabel = new Label(content, SWT.NONE);
		usernameLabel.setText("User Name:");

		final Text usernameEdit = new Text(content, SWT.BORDER);
		usernameEdit.setLayoutData("spanx, growx, wrap");

		final Label passwordLabel = new Label(content, SWT.NONE);
		passwordLabel.setText("Password:");

		final Text passwordEdit = new Text(content, SWT.BORDER | SWT.PASSWORD);
		passwordEdit.setLayoutData("spanx, growx, wrap");

		final Label serverLabel = new Label(content, SWT.NONE);
		serverLabel.setText("Server:");

		servers = new Combo(content, SWT.NONE);
		servers.setItems(Activator.getActivator().getPreferenceStore().getString(PreferenceConstants.P_SERVERS).split(","));
		servers.select(0);
		servers.setLayoutData("spanx, growx, split 2");

		final Button serverEdit = new Button(content, SWT.NONE);
		serverEdit.setText("...");
		serverEdit.setLayoutData("wrap");

		serverEdit.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				SwtDisplayUtils.async(new Runnable() {

					@Override
					public void run() {
						PreferencesUtil.createPreferenceDialogOn(PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getShell(), "com.cahoots.preferences.CahootsPreferencePage", null, null).open();

					}
				});

			}
		});

		final Button connect = new Button(content, SWT.NONE);
		connect.setText("&Connect");
		connect.setLayoutData("skip 1, split 2, tag ok");

		final Button cancel = new Button(content, SWT.NONE);
		cancel.setText("C&ancel");
		cancel.setLayoutData("tag cancel");

		// Add the listeners

		usernameEdit.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent e) {
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				if (SwtKeyUtils.enterPressed(e)) {
					SwtButtonUtils.doClick(connect);
				}
			}
		});

		passwordEdit.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent e) {
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				if (SwtKeyUtils.enterPressed(e)) {
					SwtButtonUtils.doClick(connect);
				}
			}
		});

		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setReturnCode(SWT.CANCEL);
				close();
			}
		});

		connect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				
				final String username = usernameEdit.getText();
				final String password = passwordEdit.getText();
				final String server = servers.getText();
				
				final BackgroundJob backgroundJob = new BackgroundJob() {
					@Override
					public IStatus run(final IProgressMonitor monitor) {
						socket.connect(username, password, server);
						
						final Runnable runnable = new Runnable() {
							@Override
							public void run() {
								try {
									monitor.beginTask("Connecting...", 100);
									monitor.worked(100);
									setReturnCode(SWT.OK);
									close();
								} catch (final Exception e1) {
									e1.printStackTrace();
									final String message = String.format(
											"Error connecting to server %s@%s",
											username, server);
									messageDialog.info(getShell(),
											"Connection Error", message);
								}
							}
						};

						SwtDisplayUtils.async(runnable);

						return Status.OK_STATUS;
					}
				};

				backgroundJobScheduler.schedule("Establish Connection to Cahoots",
						backgroundJob);
			}
		});

		return content;
	}
}
