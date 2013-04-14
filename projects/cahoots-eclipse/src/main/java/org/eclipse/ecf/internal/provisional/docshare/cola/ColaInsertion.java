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

public class ColaInsertion implements TransformationStrategy {

	private static final long serialVersionUID = 5192625383622519749L;
	private static ColaInsertion INSTANCE;

	private ColaInsertion() {
		// default constructor is private to enforce singleton property via
		// static factory method
	}

	public static TransformationStrategy getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ColaInsertion();
		}
		return INSTANCE;
	}

	/**
	 * Resolves two conflicting <code>ColaUpdateMessage</code>s by applying an appropriate operational transform.
	 * 
	 * If necessary, modifies <code>localAppliedMsg</code> as well to reflect knowledge of <code>remoteIncomingMsg</code> 
	 * in case more conflicting/further diverging remote messages arrive.  
	 * 
	 * @param remoteIncomingMsg message originating from remote site, generated on same document state as <code>localAppliedMsg</code>
	 * @param localAppliedMsg message already applied to local document, generation state corresponds to that of <code>remoteIncomingMsg</code>
	 * @param localMsgHighPrio determines insertion preference for same offsets, if true localAppliedMsg comes first
	 * @return operational transform of remote message, not conflicting with applied local message
	 */
	public ColaUpdateMessage getOperationalTransform(final ColaUpdateMessage remoteIncomingMsg, final ColaUpdateMessage localAppliedMsg, final boolean localMsgHighPrio) {

		final ColaUpdateMessage remoteTransformedMsg = remoteIncomingMsg;

		if (localAppliedMsg.isInsertion()) {

			if (remoteTransformedMsg.getOffset() < localAppliedMsg.getOffset()) {
				//coopt(remote(low),local(high)) --> (remote(low),local(low + high))
				localAppliedMsg.setOffset(localAppliedMsg.getOffset() + remoteTransformedMsg.getOffset());
			} else if (remoteTransformedMsg.getOffset() == localAppliedMsg.getOffset()) {
				//coopt(remote(same),local(same))
				if (localMsgHighPrio) {
					//at owner --> (remote(high),local(same))
					remoteTransformedMsg.setOffset(remoteTransformedMsg.getOffset() + localAppliedMsg.getText().length());
				} else {
					//at participant --> (remote(same),local(high))
					localAppliedMsg.setOffset(localAppliedMsg.getOffset() + remoteTransformedMsg.getText().length());
				}
			} else if (remoteTransformedMsg.getOffset() > localAppliedMsg.getOffset()) {
				//coopt(remote(high),local(low)) --> (remote(low + high),local(low))
				remoteTransformedMsg.setOffset(remoteTransformedMsg.getOffset() + localAppliedMsg.getText().length());
			}

		} else if (localAppliedMsg.isDeletion()) {

			if (remoteTransformedMsg.getOffset() <= localAppliedMsg.getOffset()) {

				localAppliedMsg.setOffset(localAppliedMsg.getOffset() + remoteTransformedMsg.getLengthOfInsertedText());

			} else if (remoteTransformedMsg.getOffset() > localAppliedMsg.getOffset()) {

				if (remoteTransformedMsg.getOffset() > (localAppliedMsg.getOffset() + localAppliedMsg.getLengthOfReplacedText())) {

					remoteTransformedMsg.setOffset(remoteTransformedMsg.getOffset() - localAppliedMsg.getLengthOfReplacedText());
				} else if (remoteTransformedMsg.getOffset() <= (localAppliedMsg.getOffset() + localAppliedMsg.getLengthOfReplacedText())) {

					//TODO test this ^#$%^#$ case
					final UpdateMessage deletionFirstMessage = new UpdateMessage(localAppliedMsg.getOffset(), remoteTransformedMsg.getOffset() - localAppliedMsg.getOffset(), localAppliedMsg.getText());
					final ColaUpdateMessage deletionFirstPart = new ColaUpdateMessage(deletionFirstMessage, localAppliedMsg.getLocalOperationsCount(), localAppliedMsg.getRemoteOperationsCount());
					localAppliedMsg.addToSplitUpRepresentation(deletionFirstPart);

					final UpdateMessage deletionSecondMessage = new UpdateMessage(localAppliedMsg.getOffset() + remoteTransformedMsg.getLengthOfInsertedText(), localAppliedMsg.getLengthOfReplacedText() - deletionFirstPart.getLengthOfReplacedText(), localAppliedMsg.getText());
					final ColaUpdateMessage deletionSecondPart = new ColaUpdateMessage(deletionSecondMessage, localAppliedMsg.getLocalOperationsCount(), localAppliedMsg.getRemoteOperationsCount());
					localAppliedMsg.addToSplitUpRepresentation(deletionSecondPart);

					localAppliedMsg.setSplitUp(true);

					remoteTransformedMsg.setOffset(localAppliedMsg.getOffset());

				}
			}
		}

		return remoteTransformedMsg;
	}
}
