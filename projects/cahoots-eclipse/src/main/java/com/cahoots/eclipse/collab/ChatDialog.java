package com.cahoots.eclipse.collab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.cahoots.connection.CahootsConnection;
import com.cahoots.connection.serialize.receive.ChatReceiveMessage;
import com.cahoots.connection.serialize.send.ChatSendMessage;
import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.indigo.misc.SwtDisplayUtils;
import com.cahoots.eclipse.indigo.misc.SwtKeyUtils;
import com.cahoots.preferences.PreferenceConstants;
import com.cahoots.util.Log;
import com.google.inject.Injector;

public class ChatDialog extends Window {

	private static final Log logger = Log.getLogger(ChatDialog.class);

	private final CahootsSocket socket;
	private final CahootsConnection connection;

	private String collaborator;

	private StyledText log;
	private StyledText message;
	private Button send;

	private File logDir;

	private static String lineSeparator = System.getProperty("line.separator");
	private final DateTimeFormatter timeStampFormat = ISODateTimeFormat
			.dateTimeNoMillis();

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

		content.setLayout(new MigLayout("fill", "",
				"[growprio 100][growprio 0]"));

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

		log.append(readLog(collaborator));
		log.setTopIndex(log.getLineCount() - 1);
		return content;
	}

	private File getLogDir() {
		if (logDir == null) {
			StringBuilder sb = new StringBuilder();

			sb.append(System.getProperty("user.home")).append(File.separator)
					.append(".cahoots").append(File.separator).append("chat")
					.append(File.separator);

			logDir = new File(sb.toString());
			logDir.mkdir();
		}
		return logDir;
	}

	private void sendMessage() {
		final String msg = message.getText().trim();
		if (!msg.isEmpty()) {
			socket.send(new ChatSendMessage(connection.getUsername(),
					collaborator, timeStampFormat.print(DateTime.now()
							.getMillis()), msg));
		}

		message.setText("");

		final String time = timeStampFormat.print(new Date().getTime());
		// TODO: Handle multiple timezones, send everything UTC and convert to
		// local timezone
		String text = time + " " + connection.getUsername() + ": " + msg
				+ System.getProperty("line.separator");
		log.append(text);

		writeToLog(collaborator, text);

	}

	private String readLog(String from) {
		StringBuilder msg = new StringBuilder();
		File logFile = new File(getLogDir(), collaborator);
		try {
			FileReader fr = new FileReader(logFile);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				msg.append(line);
				msg.append(lineSeparator);
			}
		} catch (IOException ignore) {
		}
		return msg.toString();
	}

	private void writeToLog(String from, String line) {
		if (Activator.getActivator().getPreferenceStore()
				.getBoolean(PreferenceConstants.P_SAVE_CHAT)) {
			File logFile = new File(getLogDir(), from);
			if (!logFile.exists()) {
				try {
					logFile.createNewFile();
				} catch (IOException ignore) {
					ignore.printStackTrace();
				}
			}
			BufferedWriter bw = null;
			try {
				FileWriter fw = new FileWriter(logFile, true);
				bw = new BufferedWriter(fw);
				bw.append(line);
				bw.flush();
			} catch (IOException ignore) {
				ignore.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException ignore) {
					}
				}
			}
		}
	}

	public void receiveMessage(final ChatReceiveMessage msg) {
		final String time = timeStampFormat.print(new Date().getTime());
		SwtDisplayUtils.async(new Runnable() {
			@Override
			public void run() {
				// TODO: Handle multiple timezones, send everything UTC and
				// convert to local timezone
				StringBuilder sb = new StringBuilder();
				sb.append(time).append(" ").append(msg.getFrom()).append(": ")
						.append(msg.getMessage()).append(lineSeparator);

				String message = sb.toString();
				log.append(message);
				writeToLog(msg.getFrom(), message);
			}
		});
	}
}
