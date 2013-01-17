package com.cahoots.eclipse.collab;

import java.util.ArrayList;
import java.util.Arrays;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IElementStateListener;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.connection.websocket.CahootsSocket;
import com.cahoots.eclipse.Activator;
import com.cahoots.eclipse.indigo.widget.UsersList;
import com.cahoots.events.ShareDocumentEventListener;
import com.cahoots.json.Collaborator;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.send.SendOpInsertMessage;
import com.cahoots.json.send.SendShareDocumentMessage;
import com.google.inject.Injector;

/**
 * Dialog to share documents on right click
 */
public class ShareDocumentDialog extends Window implements
		IEditorActionDelegate {

	private IEditorPart targetEditor;
	private CahootsSocket cahootsSocket;

	public ShareDocumentDialog(final Shell parentShell) {
		super(parentShell);

		Injector injector = Activator.getInjector();
		cahootsSocket = injector.getInstance(CahootsSocket.class);
	}

	@Override
	protected Control createContents(final Composite parent) {
		this.getShell().setText("Share Document(s)");

		// Layout panel
		final Composite c = parent;
		c.setLayout(new MigLayout("fill", "[growprio 100][growprio 0]",
				"[growprio 0][growprio 0][growprio 0][growprio 100][growprio 0]"));

		// Title
		final Label titleLabel = new Label(c, SWT.None);
		titleLabel.setLayoutData("wrap");
		titleLabel.setText("Title: ");

		final Text title = new Text(c, SWT.SINGLE | SWT.BORDER);
		title.setLayoutData("growx, wrap");

		// Collaborators
		final Label collaboratorsLabel = new Label(c, SWT.None);
		collaboratorsLabel.setLayoutData("wrap");
		collaboratorsLabel.setText("Collaborators: ");

		final UsersList usersList = new UsersList(c, SWT.BORDER);
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
					shareDocument(new ArrayList<Collaborator>());
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

		return super.createContents(parent);
	}

	/**
	 * TODO: Finish this method
	 * 
	 * @param collaborators
	 */
	protected void shareDocument(ArrayList<Collaborator> collaborators) {
		IElementStateListener elementStateListener = new IElementStateListener() {

			@Override
			public void elementMoved(Object originalElement, Object movedElement) {
			}

			@Override
			public void elementDirtyStateChanged(Object element, boolean isDirty) {
			}

			@Override
			public void elementDeleted(Object element) {
				System.out.println(element);
			}

			@Override
			public void elementContentReplaced(Object element) {
				System.out.println(element);
			}

			@Override
			public void elementContentAboutToBeReplaced(Object element) {
			}
		};

		final IDocumentProvider documentProvider = getTextEditor()
				.getDocumentProvider();
		documentProvider.addElementStateListener(elementStateListener);

		final String documentId = "1";
		SendShareDocumentMessage message = new SendShareDocumentMessage(
				"admin", documentId, Arrays.asList("test_1"));

		final ShareDocumentMessage response = cahootsSocket
				.sendAndWaitForResponse(message, ShareDocumentMessage.class,
						ShareDocumentEventListener.class);

		IDocumentListener documentListener = new IDocumentListener() {

			/**
			 * Handle insert operation
			 */
			@Override
			public void documentChanged(DocumentEvent event) {
				int offset = event.fOffset;
				String inserted = event.getText();
				SendOpInsertMessage insert = new SendOpInsertMessage();
				insert.setOpId(response.getOpId());
				insert.setUser("admin");
				insert.setContents(inserted);
				insert.setStart(offset);
				insert.setDocumentId(documentId);
				insert.setTickStamp(tickStamp++);
				cahootsSocket.send(insert);
			}

			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
			}
		};

		IDocument document = documentProvider.getDocument(getTextEditor()
				.getEditorInput());
		document.addDocumentListener(documentListener);
	}

	static long tickStamp = 0;

	private ITextEditor getTextEditor() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return null;
		}

		final IWorkbenchWindow ww = workbench.getActiveWorkbenchWindow();
		if (ww == null) {
			return null;
		}

		final IWorkbenchPage wp = ww.getActivePage();
		if (wp == null) {
			return null;
		}

		final IEditorPart ep = wp.getActiveEditor();
		if (ep instanceof ITextEditor) {
			return (ITextEditor) ep;
		} else if (ep != null) {
			return (ITextEditor) ep.getAdapter(ITextEditor.class);
		}

		return null;
	}

	@Override
	public void run(final IAction action) {
	}

	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
	}

	@Override
	public void setActiveEditor(final IAction action,
			final IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}
}
