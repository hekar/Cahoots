package com.cahoots.eclipse.collab.share;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.filter;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.Arrays;
import java.util.List;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.chat.CollaboratorStatus;
import com.cahoots.connection.CahootsConnection;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.indigo.widget.TextEditorTools;
import com.cahoots.eclipse.indigo.widget.UserList;
import com.cahoots.json.Collaborator;
import com.google.inject.Injector;

/**
 * Dialog to share documents
 */
public class ShareDocumentDialog extends Window {

	private ShareDocumentManager shareDocumentManager;
	private TextEditorTools textEditorTools;
	private CahootsConnection connectionDetails;

	public ShareDocumentDialog(final Shell parentShell) {
		super(parentShell);

		final Injector injector = Activator.getInjector();
		shareDocumentManager = injector.getInstance(ShareDocumentManager.class);
		textEditorTools = injector.getInstance(TextEditorTools.class);
		connectionDetails = injector.getInstance(CahootsConnection.class);
	}

	@Override
	protected Control createContents(final Composite parent) {
		this.getShell().setText("Share Document(s)");

		parent.setLayout(new MigLayout("fill"));

		// Layout panel
		final Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new MigLayout("fill", "[growprio 100][growprio 0]",
				"[growprio 0][growprio 0][growprio 100][growprio 0]"));
		c.setLayoutData("grow");

		// Title
		final Label titleLabel = new Label(c, SWT.None);
		titleLabel.setLayoutData("wrap");
		titleLabel.setText("Documents: ");

		// Collaborators
		final Label collaboratorsLabel = new Label(c, SWT.None);
		collaboratorsLabel.setLayoutData("wrap");
		collaboratorsLabel.setText("Collaborators: ");

		final UserList usersList = new UserList(c, SWT.BORDER);
		usersList.setLayoutData("grow, wrap");

		// OK buttons
		final Button ok = new Button(c, SWT.PUSH);
		ok.setEnabled(false);
		ok.setText("&OK");
		ok.setLayoutData("tag ok, split 2");
		ok.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				try {
					final List<Collaborator> collaborators = usersList
							.getSelectedUsers();

					shareDocument(collaborators);
					close();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
			}
		});

		// Cancel
		final Button cancel = new Button(c, SWT.PUSH);
		cancel.setText("&Cancel");
		cancel.setLayoutData("tag cancel");
		cancel.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				close();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
			}
		});

		// Handle selection of users on list changed
		usersList.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof StructuredSelection) {
					final StructuredSelection structuredSelection = (StructuredSelection) selection;
					@SuppressWarnings("unchecked")
					final List<Collaborator> collaborators = structuredSelection
							.toList();

					final boolean hasOnlineUser = filter(
							having(on(Collaborator.class)
									.getCollaboratorStatus(),
									not(equalTo(CollaboratorStatus.OFFLINE))),
							collaborators).size() > 0;

					ok.setEnabled(hasOnlineUser);
				} else {
					throw new IllegalStateException(
							"viewer selection is not StructuredSelection");
				}
			}
		});

		// Handle double clicking the username. Share with this user
		usersList.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(final DoubleClickEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof StructuredSelection) {
					final StructuredSelection structuredSelection = (StructuredSelection) selection;

					final Object selected = structuredSelection
							.getFirstElement();
					if (selected instanceof Collaborator) {
						final Collaborator collaborator = (Collaborator) selected;
						final boolean collaboratorOffline = collaborator
								.getCollaboratorStatus() == CollaboratorStatus.OFFLINE;
						final boolean sameUser = collaborator.getUsername()
								.equals(connectionDetails.getUsername());
						if (!collaboratorOffline && !sameUser) {
							shareDocument(Arrays.asList(collaborator));
							close();
						}
					}
				} else {
					throw new IllegalArgumentException(
							"UsersList is not using StructuredSelection list. Has this been changed?");
				}
			}
		});

		getShell().setSize(640, 600);

		return c;
	}

	private void shareDocument(final List<Collaborator> collaborators) {
		// TODO: Share documents with collaborators
		final ITextEditor textEditor = textEditorTools.getTextEditor();
		shareDocumentManager.shareDocument(textEditor, collaborators);
	}
}
