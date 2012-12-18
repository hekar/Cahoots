package com.cahoots.eclipse.op;

import java.util.TreeSet;

public class OpMemento {
	private final OpDocument document;
	private final TreeSet<OpTransformation> transformations = new TreeSet<OpTransformation>();

	public OpMemento(final OpDocument document) {
		this.document = document;
	}

	public OpDocument getDocument() {
		return document;
	}
	
	public void addTransformation(final OpTransformation transformation) {
		transformations.add(transformation);
	}

}
