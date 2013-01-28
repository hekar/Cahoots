package com.cahoots.eclipse.collab;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.indigo.log.Log;
import com.cahoots.eclipse.swt.SwtDisplayUtils;
import com.cahoots.eclipse.swt.SwtKeyUtils;
import com.cahoots.json.receive.ChatReceiveMessage;
import com.cahoots.json.send.ChatSendMessage;
import com.google.inject.Injector;

public class ChatDialog extends Window {

	private static final Log logger = Log.getLogger(ChatDialog.class);

	private final CahootsSocket socket;
	private final CahootsConnection connection;

	private String collaborator;

	private StyledText log;
	private StyledText message;
	private Button send;

	private final DateFormat timeStampFormat = DateFormat.getDateTimeInstance(
			DateFormat.MEDIUM, DateFormat.MEDIUM);
	// Old format: DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

	public ChatDialog(final Shell parent, final List<String> collaborators) {
		super(parent);

		if (collaborators.size() == 0) {
			throw new IllegalArgumentException(
					"Must have at least one collaborator");
		} else if (collaborators.size() > 1) {
			// This is unsupported right now
			throw new IllegalArgumentException(
					"Cannot have more than one collaborator, as this is not supported right now");
		}

		final Injector injector = Activator.getInjector();
		socket = injector.getInstance(CahootsSocket.class);
		connection = injector.getInstance(CahootsConnection.class);

		this.collaborator = collaborators.get(0);
	}

	@Override
	protected Control createContents(final Composite parent) {
		getShell().setText(collaborator);
		// TODO: Memorize previous size
		getShell().setSize(640, 480);

		final Composite content = parent;

		content.setLayout(new MigLayout("fill", "", "[growprio 100][growprio 0]"));

		log = new StyledText(content, SWT.BORDER | SWT.READ_ONLY);
		log.setLayoutData("grow, wrap");

		message = new StyledText(content, SWT.BORDER | SWT.MULTI);
		message.setLayoutData("growx, split 2");
		message.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent e) {
				final boolean enable = !message.getText().isEmpty();
				send.setEnabled(enable);
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				if (SwtKeyUtils.enterPressed(e)) {
					sendMessage();
				}
			}
		});
		

		if (!message.forceFocus()) {
			logger.debug("Failure to force focus on textbox in chat dialog");
		}

		send = new Button(content, SWT.PUSH);
		send.setText("send");
		send.setLayoutData("");

		send.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				sendMessage();
			}
		});

		return content;
	}

	private void sendMessage() {
		final String msg = message.getText().trim();
		if (!msg.isEmpty()) {
			socket.send(new ChatSendMessage(connection.getUsername(),
					collaborator, new Date(), msg));
		}

		message.setText("");

		final String time = timeStampFormat.format(new Date());
		// TODO: Handle multiple timezones, send everything UTC and convert to
		// local timezone
		log.append(time + " " + connection.getUsername() + ": " + msg
				+ System.getProperty("line.separator"));

	}

	public void receiveMessage(final ChatReceiveMessage msg) {
		final String time = timeStampFormat.format(new Date());
		SwtDisplayUtils.async(new Runnable() {
			@Override
			public void run() {
				// TODO: Handle multiple timezones, send everything UTC and
				// convert to local timezone
				log.append(time + " " + msg.getFrom() + ": "
						+ msg.getMessage()
						+ System.getProperty("line.separator"));
			}
		});
	}
}
