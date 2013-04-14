package com.cahoots.eclipse.test.unit.client;

import static com.cahoots.eclipse.test.utils.OpTestUtils.createConnectionDetails;
import static com.cahoots.eclipse.test.utils.OpTestUtils.createDelete;
import static com.cahoots.eclipse.test.utils.OpTestUtils.createDocument;
import static com.cahoots.eclipse.test.utils.OpTestUtils.createInsert;
import static com.cahoots.eclipse.test.utils.OpTestUtils.clearMoved;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.cahoots.connection.serialize.receive.OpDeleteMessage;
import com.cahoots.connection.serialize.receive.OpInsertMessage;
import com.cahoots.eclipse.optransformation.OpMergeManager;
import com.cahoots.eclipse.optransformation.OpSynchronizedClock;

/**
 * @author Hekar
 *
 */
public class OpTransformationTests {

	final OpSynchronizedClock clock = new OpSynchronizedClock(0);
	private static String user1 = "1";
	private static String user2 = "2";
	
	private OpMergeManager m1;
	private OpMergeManager m2;
	
	@Before
	public void initMemento() {
		m1 = new OpMergeManager(createDocument(), createConnectionDetails(user1));
		m2 = new OpMergeManager(createDocument(), createConnectionDetails(user2));
	}
	

	@Test
	public void crosswire_Insert_Insert() throws Exception {
		/** 
		 * Crosswire insert before insert
		 */
		final OpInsertMessage i0 = createInsert(user1, 1L, 0, "ace");
		final OpInsertMessage i1 = createInsert(user1, 2L, 1, "b");
		final OpInsertMessage i2 = createInsert(user2, 3L, 2, "d");
		
		m1.apply(i0);
		
		m1.sendTransformation(i0);
		m2.receiveTransformation(i0);
		
		m1.apply(i1);
		m2.apply(i2);
		
		m1.sendTransformation(i1);
		m2.sendTransformation(i2);
		
		m1.receiveTransformation(i2);
		clearMoved(i0, i1, i2);
		m2.receiveTransformation(i1);
		
		assertEquals("m2", "abcde", m2.getDocumentContent());
		assertEquals("m1", "abcde", m1.getDocumentContent());
		assertEquals("m1 == m2", m1.getDocumentContent(), m2.getDocumentContent());
	}
	
	@Test
	public void crosswire_Insert_Insert_Same() throws Exception {
		/** 
		 * Crosswire insert and insert at same positions and timestamps
		 */
		final OpInsertMessage i0 = createInsert(user1, 1L, 0, "abe");
		final OpInsertMessage i1 = createInsert(user1, 2L, 2, "c");
		final OpInsertMessage i2 = createInsert(user2, 3L, 2, "d");
		
		m1.apply(i0);
		
		m1.sendTransformation(i0);
		m2.receiveTransformation(i0);
		
		m1.apply(i1);
		m2.apply(i2);
		
		m1.sendTransformation(i1);
		m2.sendTransformation(i2);
		
		m1.receiveTransformation(i2);
		clearMoved(i0, i1, i2);
		m2.receiveTransformation(i1);
		
		assertEquals("m2", "abcde", m2.getDocumentContent());
		assertEquals("m1", "abcde", m1.getDocumentContent());
		assertEquals("m1 == m2", m1.getDocumentContent(), m2.getDocumentContent());
	}

	@Test
	public void crosswire_Insert_Delete() throws Exception {
		/** 
		 * Fill up the buffer with many characters.
		 * Begin inserting before deletions
		 */
		final OpInsertMessage i0 = createInsert(user1, 1L, 0, "abcdfg");
		final OpInsertMessage i1 = createInsert(user1, 2L, 4, "e");
		final OpDeleteMessage i2 = createDelete(user2, 2L, 5, 6);
		i2.setOldContent("g");
		
		m1.apply(i0);
		
		m1.sendTransformation(i0);
		m2.receiveTransformation(i0);
		
		m1.apply(i1);
		m2.apply(i2);
		
		m1.sendTransformation(i1);
		m2.sendTransformation(i2);

		m1.receiveTransformation(i2);
		clearMoved(i0, i1, i2);
		m2.receiveTransformation(i1);
		
		assertEquals("m2", "abcdef", m2.getDocumentContent());
		assertEquals("m1", "abcdef", m1.getDocumentContent());
		assertEquals("m1 == m2", m1.getDocumentContent(), m2.getDocumentContent());
	}

	@Test
	public void crosswire_Delete_Insert() throws Exception {
		/** 
		 * Fill up the buffer with many characters.
		 * Begin deleting before insertions
		 */
		final OpInsertMessage i0 = createInsert(user1, 1L, 0, "abcdfg");
		final OpInsertMessage i1 = createInsert(user1, 2L, 4, "e");
		final OpDeleteMessage i2 = createDelete(user2, 2L, 0, 1);
		
		m1.apply(i0);
		
		m1.sendTransformation(i0);
		m2.receiveTransformation(i0);
		
		m1.apply(i1);
		m2.apply(i2);
		
		m1.sendTransformation(i1);
		m2.sendTransformation(i2);

		m1.receiveTransformation(i2);
		clearMoved(i0, i1, i2);
		m2.receiveTransformation(i1);
		
		assertEquals("m1", "bcdefg", m1.getDocumentContent());
		assertEquals("m2", "bcdefg", m2.getDocumentContent());
		assertEquals("m1 == m2", m1.getDocumentContent(), m2.getDocumentContent());
	}

	@Test
	public void crossdiverge_Insert_Insert() throws Exception {
		/** 
		 * Many crosswires of inserts
		 */
		final OpInsertMessage i0 = createInsert(user1, 1L, 0, "abg");
		final OpInsertMessage i1 = createInsert(user1, 2L, 2, "c");
		final OpInsertMessage i2 = createInsert(user2, 3L, 2, "f");
		final OpInsertMessage i3 = createInsert(user1, 4L, 3, "d");
		final OpInsertMessage i4 = createInsert(user1, 5L, 4, "e");
		
		m1.apply(i0);
		
		m1.sendTransformation(i0);
		m2.receiveTransformation(i0);

		m1.apply(i1);
		m2.apply(i2);
		m1.apply(i3);
		m1.apply(i4);
		
		m1.sendTransformation(i1);
		m2.sendTransformation(i2);
		m1.sendTransformation(i3);
		m1.sendTransformation(i4);
		
		m1.receiveTransformation(i2);
		clearMoved(i0, i1, i2, i3, i4);
		m2.receiveTransformation(i1);
		m2.receiveTransformation(i3);
		m2.receiveTransformation(i4);
		
		assertEquals("m1", "abcdefg", m1.getDocumentContent());
		assertEquals("m2", "abcdefg", m2.getDocumentContent());
		assertEquals("m1 == m2", m1.getDocumentContent(), m2.getDocumentContent());
	}

	@Test
	public void crossdiverge_Insert_Delete() throws Exception {
		/**
		 * 
		 */
		final OpInsertMessage i0 = createInsert(user1, 1L, 0, "cde");
		final OpInsertMessage i1 = createInsert(user1, 2L, 0, "a");
		final OpInsertMessage i2 = createInsert(user1, 3L, 1, "b");
		final OpDeleteMessage i3 = createDelete(user2, 4L, 2, 3, "e");
		final OpDeleteMessage i4 = createDelete(user2, 5L, 1, 2, "d");
		
		m1.apply(i0);
		
		m1.sendTransformation(i0);
		m2.receiveTransformation(i0);
		
		m1.apply(i1);
		m1.apply(i2);
		m2.apply(i3);
		m2.apply(i4);
		
		m1.sendTransformation(i1);
		m1.sendTransformation(i2);
		m2.sendTransformation(i3);
		m2.sendTransformation(i4);
		
		m2.receiveTransformation(i1);
		m2.receiveTransformation(i2);
		clearMoved(i0, i1, i2, i3, i4);
		m1.receiveTransformation(i3);
		m1.receiveTransformation(i4);
		
		//assertEquals("m1", "abc", m1.getDocumentContent());
		//assertEquals("m2", "abc", m2.getDocumentContent());
		assertEquals("m1 == m2", m1.getDocumentContent(), m2.getDocumentContent());
	}

	@Test
	public void crossdiverge_Delete_Insert() throws Exception {
		/**
		 * 
		 */
		final OpInsertMessage i0 = createInsert(user1, 1L, 0, "abcdef");
		final OpDeleteMessage i1 = createDelete(user2, 2L, 1, 2, "b");
		final OpDeleteMessage i2 = createDelete(user2, 3L, 1, 2, "c");
		final OpInsertMessage i3 = createInsert(user1, 4L, 4, "g");
		final OpInsertMessage i4 = createInsert(user1, 5L, 4, "h");
		
		m1.apply(i0);
		
		m1.sendTransformation(i0);
		m2.receiveTransformation(i0);
		
		m2.apply(i1);
		m2.apply(i2);
		m1.apply(i3);
		m1.apply(i4);
		
		m2.sendTransformation(i1);
		m2.sendTransformation(i2);
		m1.sendTransformation(i3);
		m1.sendTransformation(i4);
		
		m1.receiveTransformation(i1);
		m1.receiveTransformation(i2);
		clearMoved(i0, i1, i2, i3, i4);
		m2.receiveTransformation(i3);
		m2.receiveTransformation(i4);
		
		assertEquals("m2", "adhgef", m2.getDocumentContent());
		assertEquals("m1", "adhgef", m1.getDocumentContent());
		assertEquals("m1 == m2", m1.getDocumentContent(), m2.getDocumentContent());
	}
	
	@Test
	public void crossoverlap_Delete_Insert() throws Exception {
		/**
		 * Crosswire of a delete over an insert
		 * 
		 * Example:
		 * 	abcefg
		 * 
		 *  User A: Deletes 'cef'
		 * 	User B: Inserts 'd' after c
		 * 	
		 * 	Crosswire occurs. 
		 * 		'd' should be placed before 'g', not after
		 */
		throw new Exception();
	}
}