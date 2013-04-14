package com.cahoots.eclipse.optransformation;

import org.eclipse.ui.texteditor.ITextEditor;

public class OpDocument {
	private final String opId;
	private final String documentId;
	private ITextEditor textEditor;

	public OpDocument(final String opId, final String documentId,
			final ITextEditor textEditor) {
		this.opId = opId;
		this.documentId = documentId;
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
		final String[] split = documentId.split("/");
		return split[split.length - 1];
	}

	public ITextEditor getTextEditor() {
		return textEditor;
	}

	public void setTextEditor(final ITextEditor textEditor) {
		this.textEditor = textEditor;
	}

}
