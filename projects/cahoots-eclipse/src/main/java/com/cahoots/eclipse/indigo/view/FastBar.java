package com.cahoots.eclipse.indigo.view;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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
	private Label barmsg;
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
		bar.setToolTipText("Connect to Cahoots");
		barmsg = new Label(bar, SWT.NONE);

		barImg = new Label(bar, SWT.NONE);

		try {
			barImg.setImage(ImageDescriptor.createFromURL(
					new URL(bundle.getEntry("/"), "icons/" + "black_logo.gif"))
					.createImage());
		} catch (final Exception e) {
			e.printStackTrace();
		}

		final MouseListener m = new MouseListener() {

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
		};

		bar.addMouseListener(m);
		barImg.addMouseListener(m);

		final CahootsSocket socket = injector.getInstance(CahootsSocket.class);
		socket.addConnectEventListener(new ConnectEventListener() {
			@Override
			public void connected(final ConnectEvent event) {
				SwtDisplayUtils.async(new Runnable() {
					@Override
					public void run() {
						bar.setToolTipText(String.format("Connected as %s@%s",
								details.getUsername(), details.getServer()));
						barmsg.setText(String.format("%s@%s",
								details.getUsername(), details.getServer()));

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
						bar.setToolTipText(String.format("Connected as %s@%s",
								details.getUsername(), details.getServer()));
						barmsg.setText(String.format("%s@%s",
								details.getUsername(), details.getServer()));
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
		comp.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {

				final GC g = e.gc;
				g.setForeground(Display.getCurrent().getSystemColor(
						SWT.COLOR_DARK_GRAY));
				g.drawRectangle(0, 0, e.width - 1, e.height - 1);
			}
		});

		final RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.pack = true;
		layout.wrap = false;
		comp.setLayout(layout);
		return comp;
	}
}
