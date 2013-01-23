package com.cahoots.eclipse.collab.share;

import java.util.List;

import net.miginfocom.swt.MigLayout;

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

	public ShareDocumentDialog(final Shell parentShell) {
		super(parentShell);

		Injector injector = Activator.getInjector();
		shareDocumentManager = injector.getInstance(ShareDocumentManager.class);
		textEditorTools = injector.getInstance(TextEditorTools.class);
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
		final List<Collaborator> collaborators = usersList.getSelectedUsers();
		usersList.setLayoutData("grow, wrap");

		// ok/cancel buttons
		final Button ok = new Button(c, SWT.PUSH);
		ok.setText("&OK");
		ok.setLayoutData("tag ok, split 2");
		ok.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				try {
					// TODO: Share documents with collaborators
					ITextEditor textEditor = textEditorTools.getTextEditor();
					shareDocumentManager.shareDocument(textEditor, collaborators);
					ShareDocumentDialog.this.getShell().dispose();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
			}
		});

		final Button cancel = new Button(c, SWT.PUSH);
		cancel.setText("&Cancel");
		cancel.setLayoutData("tag cancel");
		cancel.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				ShareDocumentDialog.this.getShell().dispose();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
			}
		});

		getShell().setSize(640, 600);

		return c;
	}
}
