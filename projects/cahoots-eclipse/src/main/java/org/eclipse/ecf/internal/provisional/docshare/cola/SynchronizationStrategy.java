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


import java.util.List;

public interface SynchronizationStrategy {

	public UpdateMessage registerOutgoingMessage(UpdateMessage localMsg);

	public List transformIncomingMessage(UpdateMessage remoteMsg, final boolean isInitiator);

}
