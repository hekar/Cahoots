package com.cahoots.eclipse.test.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;

import com.cahoots.connection.ConnectionDetails;
import com.cahoots.connection.serialize.receive.OpDeleteMessage;
import com.cahoots.connection.serialize.receive.OpInsertMessage;
import com.cahoots.connection.serialize.receive.OpReplaceMessage;
import com.cahoots.connection.serialize.send.SendOpDeleteMessage;
import com.cahoots.connection.serialize.send.SendOpInsertMessage;
import com.cahoots.connection.serialize.send.SendOpReplaceMessage;
import com.cahoots.eclipse.optransformation.OpDocument;
import com.cahoots.eclipse.optransformation.OpTransformation;

public class OpTestUtils {
	
	public static ConnectionDetails createConnectionDetails(final String user) {
		final ConnectionDetails cd = new ConnectionDetails();
		cd.setUsername(user);
		return cd;
	}

	public static OpDocument createDocument() {
		final IDocument document = new StringBuilderDocument();

		final OpDocument doc = mock(OpDocument.class);
		when(doc.getFilename()).thenReturn("TestFile1.java");
		when(doc.getDocumentId()).thenReturn(
				"/testSolution/test/TestFile1.java");
		when(doc.getDocument()).thenReturn(document);
		when(doc.getTextEditor()).thenReturn(null);

		return doc;
	}

	public static OpInsertMessage createInsert(final String user, final long tickStamp, final int start,
			final String content) {
		final SendOpInsertMessage msg = new SendOpInsertMessage();
		msg.setUser(user);
		msg.setStart(start);
		msg.setTickStamp(tickStamp);
		msg.setOpId("0");
		msg.setDocumentId("");
		msg.setContent(content);
		return msg;
	}

	public static OpReplaceMessage createReplace(final String user, final long tickStamp,
			final int start, final int end, final String content) {
		final SendOpReplaceMessage msg = new SendOpReplaceMessage();
		msg.setUser(user);
		msg.setStart(start);
		msg.setReplacementLength(end);
		msg.setTickStamp(tickStamp);
		msg.setOpId("0");
		msg.setDocumentId("");
		msg.setContent(content);
		return msg;
	}

	public static OpDeleteMessage createDelete(final String user, final long tickStamp, final int start,
			final int end) {
		final SendOpDeleteMessage msg = new SendOpDeleteMessage();
		msg.setUser(user);
		msg.setStart(start);
		msg.setReplacementLength(end);
		msg.setTickStamp(tickStamp);
		msg.setOpId("0");
		msg.setDocumentId("");
		return msg;
	}
	
	public static OpDeleteMessage createDelete(final String user, final long tickStamp, final int start,
			final int end, final String oldContent) {
		final SendOpDeleteMessage msg = new SendOpDeleteMessage();
		msg.setUser(user);
		msg.setStart(start);
		msg.setReplacementLength(end);
		msg.setTickStamp(tickStamp);
		msg.setOpId("0");
		msg.setDocumentId("");
		msg.setOldContent(oldContent);
		return msg;
	}
	
	public static void clearMoved(OpTransformation ... trans) {
		for (OpTransformation t : trans) {
			t.setMoved(0);
		}
	}

	private static class StringBuilderDocument implements IDocument {

		private StringBuilder sb = new StringBuilder();

		@Override
		public char getChar(final int offset) throws BadLocationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getLength() {
			return sb.length();
		}

		@Override
		public String get() {
			return sb.toString();
		}

		@Override
		public String get(final int offset, final int length) throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void set(final String text) {
			// TODO Auto-generated method stub

		}

		@Override
		public void replace(final int offset, final int length, final String text)
				throws BadLocationException {
			if (length == 0) {
				sb.insert(offset, text);
			} else if (text == null || text.isEmpty()) {
				sb.delete(offset, offset + length);
			} else {
				sb.replace(offset, offset + length, text);
			}
		}

		@Override
		public void addDocumentListener(final IDocumentListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeDocumentListener(final IDocumentListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addPrenotifiedDocumentListener(
				final IDocumentListener documentAdapter) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removePrenotifiedDocumentListener(
				final IDocumentListener documentAdapter) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addPositionCategory(final String category) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removePositionCategory(final String category)
				throws BadPositionCategoryException {
			// TODO Auto-generated method stub

		}

		@Override
		public String[] getPositionCategories() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean containsPositionCategory(final String category) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void addPosition(final Position position) throws BadLocationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void removePosition(final Position position) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addPosition(final String category, final Position position)
				throws BadLocationException, BadPositionCategoryException {
			// TODO Auto-generated method stub

		}

		@Override
		public void removePosition(final String category, final Position position)
				throws BadPositionCategoryException {
			// TODO Auto-generated method stub

		}

		@Override
		public Position[] getPositions(final String category)
				throws BadPositionCategoryException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean containsPosition(final String category, final int offset, final int length) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int computeIndexInCategory(final String category, final int offset)
				throws BadLocationException, BadPositionCategoryException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void addPositionUpdater(final IPositionUpdater updater) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removePositionUpdater(final IPositionUpdater updater) {
			// TODO Auto-generated method stub

		}

		@Override
		public void insertPositionUpdater(final IPositionUpdater updater, final int index) {
			// TODO Auto-generated method stub

		}

		@Override
		public IPositionUpdater[] getPositionUpdaters() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String[] getLegalContentTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getContentType(final int offset) throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ITypedRegion getPartition(final int offset)
				throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ITypedRegion[] computePartitioning(final int offset, final int length)
				throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addDocumentPartitioningListener(
				final IDocumentPartitioningListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeDocumentPartitioningListener(
				final IDocumentPartitioningListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setDocumentPartitioner(final IDocumentPartitioner partitioner) {
			// TODO Auto-generated method stub

		}

		@Override
		public IDocumentPartitioner getDocumentPartitioner() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getLineLength(final int line) throws BadLocationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getLineOfOffset(final int offset) throws BadLocationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getLineOffset(final int line) throws BadLocationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public IRegion getLineInformation(final int line) throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IRegion getLineInformationOfOffset(final int offset)
				throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getNumberOfLines() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getNumberOfLines(final int offset, final int length)
				throws BadLocationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int computeNumberOfLines(final String text) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String[] getLegalLineDelimiters() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getLineDelimiter(final int line) throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int search(final int startOffset, final String findString,
				final boolean forwardSearch, final boolean caseSensitive, final boolean wholeWord)
				throws BadLocationException {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
