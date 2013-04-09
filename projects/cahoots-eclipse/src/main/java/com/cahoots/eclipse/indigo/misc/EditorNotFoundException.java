package com.cahoots.eclipse.indigo.misc;

/**
 * Failure to find an editor for the document type 
 * @author Hekar
 *
 */
public class EditorNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7881422992239059653L;

	public EditorNotFoundException() {
		super();
	}

	public EditorNotFoundException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EditorNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public EditorNotFoundException(final String message) {
		super(message);
	}

	public EditorNotFoundException(final Throwable cause) {
		super(cause);
	}

}
