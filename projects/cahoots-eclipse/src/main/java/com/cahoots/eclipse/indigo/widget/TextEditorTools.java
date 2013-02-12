package com.cahoots.eclipse.indigo.widget;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.eclipse.indigo.editor.EditorNotFoundException;
import com.cahoots.java.lang.ExceptionUtils;

public class TextEditorTools {
	public ITextEditor getTextEditor() {
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
		return editorPartToTextEditor(ep);
	}

	public ITextEditor editorPartToTextEditor(final IEditorPart ep) {
		if (ep instanceof ITextEditor) {
			return (ITextEditor) ep;
		} else if (ep != null) {
			return (ITextEditor) ep.getAdapter(ITextEditor.class);
		} else {
			return null;
		}
	}

	public IEditorPart openEditor(final IWorkbenchPage workbenchPage,
			final IEditorInput editorInput, final String editorId) {
		try {
			final IWorkbenchPage page = workbenchPage;
			final IEditorPart editor = page.openEditor(editorInput, editorId);

			if (editor == null) {
				throw new EditorNotFoundException(
						"Editor was not found or external editor opened");
			}

			return editor;

		} catch (final PartInitException e) {
			throw ExceptionUtils.toRuntime(e);
		}
	}

	public IEditorPart openEditor(final ExecutionEvent executionEvent,
			final IEditorInput editorInput, final String editorId) {
		final IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(
				executionEvent).getActivePage();
		return openEditor(page, editorInput, editorId);
	}

	public IEditorPart openEditor(final ViewPart viewPart,
			final IEditorInput editorInput, final String editorId) {
		final IWorkbenchPage page = viewPart.getViewSite().getPage();
		return openEditor(page, editorInput, editorId);
	}

}
