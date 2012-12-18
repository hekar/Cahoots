package com.cahoots.eclipse.collab;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.eclipse.indigo.widget.UsersList;

/**
 * Dialog to share documents on right click
 */
public class ShareDocumentDialog extends Window {

    public ShareDocumentDialog(final Shell parentShell) {
	super(parentShell);
    }

    @Override
    protected Control createContents(final Composite parent) {
	this.getShell().setText("Share Document(s)");

	// Layout panel
	final Composite c = new Composite(parent, SWT.NONE);
	c.setLayout(new MigLayout("fill"));

	// TODO: Label

	// Title
	final Text title = new Text(c, SWT.SINGLE | SWT.BORDER);
	title.setLayoutData("growx, wrap");

	final UsersList usersList = new UsersList(c, SWT.BORDER);
	usersList.setLayoutData("grow, wrap");

	// ok/cancel buttons
	final Button ok = new Button(c, SWT.PUSH);
	ok.setText("OK");
	ok.setLayoutData("tag ok");
	ok.addSelectionListener(new SelectionListener() {
	    @Override
	    public void widgetSelected(final SelectionEvent arg0) {
		try {
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
	cancel.setText("Cancel");
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

	return super.createContents(parent);
    }

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
}
