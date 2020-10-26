package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LinkedListIndexedCollectionTest {

	@Test
	void testAdd() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add(1);
		assertThrows(NullPointerException.class, () -> {
			l1.add(null);
		});
		assertTrue(l1.contains(1));
		assertEquals(1, l1.get(0));
		l1.add("Chuck Norris");
		assertTrue(l1.contains("Chuck Norris"));
		assertEquals("Chuck Norris", l1.get(1));
	}
	
	@Test
	void testAddAll() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add(10);
		l1.add(21);
		l1.add("Miš");
		LinkedListIndexedCollection l2 = new LinkedListIndexedCollection();
		l2.addAll(l1);
		assertTrue(l2.contains(10) && l2.contains(21) && l2.contains("Miš")); 
	}
	
	@Test
	void testRemove1() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add(2.3);
		l1.add(true);
		l1.add("Paris");
		assertTrue(l1.remove(true));
		assertEquals("Paris", l1.get(1));
	}
	
	@Test
	void testGet() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add('t');
		l1.add(1.223);
		l1.add(true);
		assertThrows(IndexOutOfBoundsException.class, ()-> {
			l1.get(6);
		});
		assertEquals(1.223, l1.get(1));
	}
	
	@Test
	void testSize() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		assertEquals(0, l1.size());
		l1.add(34);
		l1.add(false);
		l1.add("Stop");
		assertEquals(3, l1.size());
	}
	
	@Test
	void testClear() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add(2223);
		l1.add("London");
		l1.add("Šišmiš");
		l1.clear();
		assertEquals(0, l1.size());
		assertThrows(IndexOutOfBoundsException.class, () -> {
			l1.get(1);
		});
		assertFalse(l1.contains(2223));
	}
	
	@Test
	void testIndexOf() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add(10);
		l1.add(21);
		l1.add("Miš");
		assertEquals(0, l1.indexOf(10));
		assertEquals(-1, l1.indexOf(null));
	}
	
	void testForEach() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add(10);
		l1.add(21);
		l1.add("Miš");
		l1.forEach(new Processor() {
			@Override
			public void process(Object value) {
				value = "Switch";
			}
		});
		assertEquals(l1.contains("Switch"), true);
		assertEquals(l1.get(2), "Switch");
	}
	
	@Test
	void testInsert() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add(1);
		l1.add(2);
		l1.add("Šišmiš");
		l1.insert(3, 1);
		assertEquals(3, l1.get(1));
		assertEquals(4, l1.size());
		assertEquals("Šišmiš", l1.get(3));
	}
	
	@Test
	void testRemove2() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add(1);
		l1.add(2);
		l1.add("Šišmiš");
		l1.remove(1);
		assertEquals(2, l1.size());
		assertEquals("Šišmiš", l1.get(1));
	}
	
	@Test
	void testToArray() {
		LinkedListIndexedCollection l1 = new LinkedListIndexedCollection();
		l1.add(1);
		l1.add(2);
		l1.add("Šišmiš");
		Object[] o = l1.toArray();
		assertEquals(1, o[0]);
		assertEquals(2, o[1]);
		assertEquals("Šišmiš", o[2]);
		assertEquals(3, o.length);
	}
}
