package com.cahoots.eclipse.optransformation;

import com.cahoots.connection.serialize.receive.OpDeleteMessage;
import com.cahoots.connection.serialize.receive.OpInsertMessage;
import com.cahoots.util.Tracer;

/**
 * Handle merging operational transform conflicts.
 * 
 * This code is based on the Cola algorithm from the Eclipse foundation.
 * 
 * References:
 * 	http://download.ecf-project.org/repo/zEclipseGitCloner/workspace/framework/bundles/org.eclipse.ecf.sync/src/org/eclipse/ecf/internal/sync/doc/cola/ColaInsertionTransformationStategy.java
 * 	http://download.ecf-project.org/repo/zEclipseGitCloner/workspace/framework/bundles/org.eclipse.ecf.sync/src/org/eclipse/ecf/internal/sync/doc/cola/ColaDeletionTransformationStrategy.java 
 */
public class OpConflictStrategy {

	private Tracer out;

	/**
	 * 
	 * @param tracerLog
	 *            - Log file for tracing conflict merges
	 */
	public OpConflictStrategy(final Tracer tracerLog) {
		this.out = tracerLog;
	}

	/**
	 * Merge conflicts between a local and remote transformation in a document
	 * 
	 * @param document
	 * @param remote
	 * @param local
	 */
	public void mergeConflicts(final OpDocument document, final OpTransformation remote, final OpTransformation local) {
		if (remote instanceof OpDeleteMessage) {
			out.writeln("Remote: %s Delete", remote.getUser());
			if (local instanceof OpDeleteMessage) {
				remoteDeleteLocalDelete(remote, local);
			} else if (local instanceof OpInsertMessage) {
				remoteDeleteLocalInsert(remote, local);
			}
		} else if (remote instanceof OpInsertMessage) {
			out.writeln("Remote: %s Insert", remote.getUser());
			if (local instanceof OpInsertMessage) {
				remoteInsertLocalInsert(remote, local);
			} else if (local instanceof OpDeleteMessage) {
				remoteInsertLocalDelete(remote, local);
			}
		}
	}

	/**
	 * Merge conflicts between a remote delete and a local delete
	 * 
	 * @param remote
	 * @param local
	 */
	private void remoteDeleteLocalDelete(final OpTransformation remote, final OpTransformation local) {
		final int noOpLength = 0;

		if (remote.getIndex() < local.getIndex()) {

			if ((remote.getIndex() + remote.getReplacementLength()) < local.getIndex()) {

				local.setIndex(local.getIndex() - remote.getReplacementLength());

			} else if ((remote.getIndex() + remote.getReplacementLength()) < (local.getIndex() + local
					.getReplacementLength())) {
				remote.setReplacementLength((remote.getIndex() + remote.getReplacementLength()) - local.getIndex());
				local.setReplacementLength((remote.getIndex() + remote.getReplacementLength()) - local.getIndex());
				local.setIndex(remote.getIndex());// TODO verify!

			} else if ((remote.getIndex() + remote.getReplacementLength()) >= (local.getIndex() + local
					.getReplacementLength())) {
				remote.setReplacementLength(remote.getReplacementLength() - local.getReplacementLength());
				local.setIndex(remote.getIndex());
				local.setReplacementLength(noOpLength);
			}
		} else if (remote.getIndex() == local.getIndex()) {

			if ((remote.getIndex() + remote.getReplacementLength()) < (local.getIndex() + local.getReplacementLength())) {
				remote.setReplacementLength(noOpLength);
				local.setReplacementLength(local.getReplacementLength() - remote.getReplacementLength());
			} else if ((remote.getIndex() + remote.getReplacementLength()) == (local.getIndex() + local
					.getReplacementLength())) {
				remote.setReplacementLength(noOpLength);
				local.setReplacementLength(noOpLength);
			} else if ((remote.getIndex() + remote.getReplacementLength()) > (local.getIndex() + local
					.getReplacementLength())) {
				remote.setReplacementLength(remote.getReplacementLength() - local.getReplacementLength());
				local.setReplacementLength(noOpLength);
			}
		} else if (remote.getIndex() > local.getIndex()) {

			if (remote.getIndex() > (local.getIndex() + local.getReplacementLength())) {
				remote.setIndex(remote.getIndex() - local.getReplacementLength());

			} else if ((remote.getIndex() + remote.getReplacementLength()) < (local.getIndex() + local
					.getReplacementLength())) {
				remote.setReplacementLength(noOpLength);
				local.setReplacementLength(local.getReplacementLength() - remote.getReplacementLength());

			} else if (remote.getIndex() < (local.getIndex() + local.getReplacementLength())) {
				remote.setReplacementLength(remote.getReplacementLength()
						- (local.getIndex() + local.getReplacementLength()) - remote.getIndex());
				remote.setIndex(local.getIndex());

				local.setReplacementLength(local.getReplacementLength()
						- (local.getIndex() + local.getReplacementLength()) - remote.getIndex());
			}
		}
	}

	/**
	 * Merge conflicts between a remote delete and a local insert
	 * 
	 * @param remote
	 * @param local
	 */
	private void remoteDeleteLocalInsert(final OpTransformation remote, final OpTransformation local) {
		if (remote.getIndex() < local.getIndex()) {
			if ((remote.getIndex() + remote.getReplacementLength()) < local.getIndex()) {
				local.setIndex(local.getIndex() - remote.getReplacementLength());
				out.writeln(
						"Local: %s Insert - (remote.getIndex() + remote.getReplacementLength()) < local.getIndex()",
						local.getUser());
			} else if ((remote.getIndex() + remote.getReplacementLength()) >= local.getIndex()) {

				// TODO: Make sure this case works with a unit test

				// remote deletion reaches into local insertion and
				// potentially over it
				// remote deletion needs to be split apart
				// final UpdateMessage deletionFirstMsg = new
				// UpdateMessage(remoteTransformedMsg.getOffset(),
				// movedBy.getIndex() - moved.getIndex(),
				// remoteTransformedMsg.getText());
				// final ColaUpdateMessage deletionFirstPart = new
				// ColaUpdateMessage(deletionFirstMsg,
				// remoteTransformedMsg.getLocalOperationsCount(),
				// remoteTransformedMsg.getRemoteOperationsCount());
				// remoteTransformedMsg.addToSplitUpRepresentation(deletionFirstPart);
				//
				// final UpdateMessage deletionSecondMsg = new
				// UpdateMessage(localAppliedMsg.getOffset() +
				// localAppliedMsg.getLengthOfInsertedText(),
				// remoteTransformedMsg.getLengthOfReplacedText() -
				// deletionFirstPart.getLengthOfReplacedText(),
				// remoteTransformedMsg.getText());
				// final ColaUpdateMessage deletionSecondPart = new
				// ColaUpdateMessage(deletionSecondMsg,
				// remoteTransformedMsg.getLocalOperationsCount(),
				// remoteTransformedMsg.getRemoteOperationsCount());
				// remoteTransformedMsg.addToSplitUpRepresentation(deletionSecondPart);
				//
				// remoteTransformedMsg.setSplitUp(true);
				//
				// //local insertion needs to be moved left by overlap
				// localAppliedMsg.setOffset(remoteTransformedMsg.getOffset());

				out.writeln("Local: Insert - (remote.getIndex() + remote.getReplacementLength()) >= local.getIndex()");
			}
		} else if (remote.getIndex() >= local.getIndex()) {
			remote.setIndex(remote.getIndex() + local.getLength());
			out.writeln("Local: %s Insert - remote.getIndex() >= local.getIndex()", local.getUser());
		}
	}

	/**
	 * Merge conflicts between a remote insert and a local insert
	 * 
	 * @param remote
	 * @param local
	 */
	private void remoteInsertLocalInsert(final OpTransformation remote, final OpTransformation local) {
		if (remote.getIndex() < local.getIndex()) {
			local.setIndex(local.getIndex() + remote.getLength());

			out.writeln("Local: Insert - remote.getIndex() < local.getIndex()");
		} else if (remote.getIndex() == local.getIndex()) {
			out.writeln("Local: Insert - remote.getIndex() == local.getIndex()");

			final int compare = local.getUser().compareTo(remote.getUser());
			if (compare < 0) {
				remote.setIndex(remote.getIndex() + local.getLength());
			} else {
				local.setIndex(local.getIndex() + remote.getLength());
			}
		} else if (remote.getIndex() > local.getIndex()) {
			remote.setIndex(remote.getIndex() + local.getLength());
			out.writeln("Local: Insert - remote.getIndex() > local.getIndex()");
		}
	}

	/**
	 * Merge conflicts between a remote insert and a local delete
	 * 
	 * @param remote
	 * @param local
	 */
	private void remoteInsertLocalDelete(final OpTransformation remote, final OpTransformation local) {
		if (remote.getIndex() <= local.getIndex()) {

			local.setIndex(local.getIndex() + remote.getLength());

			out.writeln("Local: Delete - remote.getIndex() <= local.getIndex()");
		} else if (remote.getIndex() > local.getIndex()) {

			if (remote.getIndex() > (local.getIndex() + local.getReplacementLength())) {

				remote.setIndex(remote.getIndex() - local.getReplacementLength());

				out.writeln("Local: Delete - remote.getIndex() > (local.getIndex() + local.getReplacementLength())");
			} else if (remote.getIndex() <= (local.getIndex() + local.getReplacementLength())) {

				// TODO: Make sure this case works with a unit test

				// final UpdateMessage deletionFirstMessage = new
				// UpdateMessage(movedBy.getIndex(),
				// moved.getIndex() - movedBy.getIndex(),
				// movedBy.getText());
				// final ColaUpdateMessage deletionFirstPart = new
				// ColaUpdateMessage(deletionFirstMessage,
				// movedBy.getLocalOperationsCount(),
				// movedBy.getRemoteOperationsCount());
				// movedBy.addToSplitUpRepresentation(deletionFirstPart);
				//
				// final UpdateMessage deletionSecondMessage = new
				// UpdateMessage(movedBy.getIndex() +
				// moved.getLengthOfInsertedText(),
				// movedBy.getLengthOfReplacedText() -
				// deletionFirstPart.getLengthOfReplacedText(),
				// movedBy.getText());
				// final ColaUpdateMessage deletionSecondPart = new
				// ColaUpdateMessage(deletionSecondMessage,
				// movedBy.getLocalOperationsCount(),
				// movedBy.getRemoteOperationsCount());
				// movedBy.addToSplitUpRepresentation(deletionSecondPart);
				//
				// movedBy.setSplitUp(true);
				//
				// moved.setIndex(movedBy.getIndex());

				out.writeln("Local: Delete - remote.getIndex() <= (local.getIndex() + local.getReplacementLength())");
			}
		}
	}

}
