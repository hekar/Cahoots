/****************************************************************************
 * Copyright (c) 2008 Mustafa K. Isik and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mustafa K. Isik - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.provisional.docshare.cola;

//TODO make this to be something like a marker interface, does not need to be a class
public class ColaReplacement implements TransformationStrategy {

	private static final long serialVersionUID = -7295023855308474804L;
	private static ColaReplacement INSTANCE;

	private ColaReplacement() {
		// default constructor is private to enforce singleton property via
		// static factory method
	}

	public static TransformationStrategy getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ColaReplacement();
		}
		return INSTANCE;
	}

	public ColaUpdateMessage getOperationalTransform(final ColaUpdateMessage remoteMsg, final ColaUpdateMessage appliedLocalMsg, final boolean localMsgHighPrio) {
		return null;
	}

}
