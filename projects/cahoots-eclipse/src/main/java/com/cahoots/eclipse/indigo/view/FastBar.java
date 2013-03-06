package com.cahoots.eclipse.indigo.view;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.osgi.framework.Bundle;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.indigo.popup.ConnectStuff;
import com.cahoots.eclipse.swt.SwtDisplayUtils;
import com.cahoots.events.ConnectEvent;
import com.cahoots.events.ConnectEventListener;
import com.cahoots.events.DisconnectEvent;
import com.cahoots.events.DisconnectEventListener;
import com.google.inject.Injector;

public class FastBar extends WorkbenchWindowControlContribution {

	private Composite bar;
	private Label barImg;
	private Label usrImg;
	private Bundle bundle;
	private Injector injector;
	private CahootsConnection details;
	private ConnectStuff stuff;

	@Override
	protected Control createControl(final Composite parent) {

		injector = Activator.getInjector();
		bundle = injector.getInstance(Bundle.class);
		stuff = Activator.getInjector().getInstance(ConnectStuff.class);

		details = injector.getInstance(CahootsConnection.class);
		bar = createLoginTrim(parent);

		barImg = new Label(bar, SWT.NONE);
		usrImg = new Label(bar, SWT.NONE);
		try {
			barImg.setImage(ImageDescriptor.createFromURL(
					new URL(bundle.getEntry("/"), "icons/" + "black_logo.gif"))
					.createImage());
		} catch (final Exception e) {
		}

		try {
			usrImg.setImage(ImageDescriptor.createFromURL(
					new URL(bundle.getEntry("/"), "icons/" + "user.gif"))
					.createImage());
		} catch (final MalformedURLException e) {
			// ignore
		}

		barImg.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(final MouseEvent arg0) {
			}

			@Override
			public void mouseDown(final MouseEvent arg0) {
			}

			@SuppressWarnings("deprecation")
			@Override
			public void mouseDoubleClick(final MouseEvent arg0) {
				if (details.isAuthenticated()) {
					stuff.disconnect();
				} else {
					stuff.connect();
				}
			}
		});

		usrImg.setToolTipText("View users list");

		usrImg.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(final MouseEvent arg0) {

			}

			@Override
			public void mouseDown(final MouseEvent arg0) {

			}

			@Override
			public void mouseDoubleClick(final MouseEvent arg0) {
				try {
					PlatformUI
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getActivePage()
							.showView(
									"com.cahoots.eclipse.indigo.view.UsersView");
				} catch (final PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		final CahootsSocket socket = injector.getInstance(CahootsSocket.class);
		socket.addConnectEventListener(new ConnectEventListener() {
			@Override
			public void connected(final ConnectEvent event) {
				SwtDisplayUtils.async(new Runnable() {
					@Override
					public void run() {
						barImg.setToolTipText(String.format(
								"Connected as %s@%s", details.getUsername(),
								details.getServer()));

						try {
							barImg.setImage(ImageDescriptor.createFromURL(
									new URL(bundle.getEntry("/"), "icons/"
											+ "green_logo.gif")).createImage());
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		socket.addDisconnectEventListener(new DisconnectEventListener() {

			@Override
			public void onEvent(final DisconnectEvent msg) {
				final CahootsConnection details = injector
						.getInstance(CahootsConnection.class);
				SwtDisplayUtils.async(new Runnable() {
					@Override
					public void run() {
						barImg.setToolTipText(String.format(
								"Connected as %s@%s", details.getUsername(),
								details.getServer()));
						try {
							barImg.setImage(ImageDescriptor.createFromURL(
									new URL(bundle.getEntry("/"), "icons/"
											+ "black_logo.gif")).createImage());
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		return bar;
	}

	private Composite createLoginTrim(final Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);

		final RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.pack = true;
		layout.wrap = false;
		comp.setLayout(layout);
		return comp;
	}
}
