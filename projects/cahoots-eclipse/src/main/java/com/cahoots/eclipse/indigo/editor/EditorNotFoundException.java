package com.cahoots.eclipse.indigo.editor;

public class EditorNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7881422992239059653L;

	public EditorNotFoundException() {
		super();
	}

	public EditorNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EditorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EditorNotFoundException(String message) {
		super(message);
	}

	public EditorNotFoundException(Throwable cause) {
		super(cause);
	}

}
