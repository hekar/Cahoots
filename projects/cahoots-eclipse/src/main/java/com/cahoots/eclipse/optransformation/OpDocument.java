package com.cahoots.eclipse.optransformation;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.ITextEditor;

import com.cahoots.util.ExceptionUtils;

public class OpDocument {
	private final String opId;
	private final String documentId;
	private ITextEditor textEditor;
	private final IDocument document;

	public OpDocument(final String opId, final String documentId,
			IDocument document, final ITextEditor textEditor) {
		this.opId = opId;
		this.documentId = documentId;
		this.document = document;
		this.textEditor = textEditor;
	}

	public String getOpId() {
		return opId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public String getFilename() {
		// TODO: Add error handling
		return documentIdToFilename(documentId);
	}
	
	public static String documentIdToFilename(String documentId) {
		final String[] split = documentId.split("/");
		return split[split.length - 1];
	}

	public ITextEditor getTextEditor() {
		return textEditor;
	}

	public void setTextEditor(final ITextEditor textEditor) {
		this.textEditor = textEditor;
	}

	public IDocument getDocument() {
		return document;
	}

	public void replace(Integer index, int length, String text) {
		try {
			this.document.replace(index, length, text);
		} catch (BadLocationException e) {
			e.printStackTrace();
			ExceptionUtils.rethrow(e);
		}
	}

}
