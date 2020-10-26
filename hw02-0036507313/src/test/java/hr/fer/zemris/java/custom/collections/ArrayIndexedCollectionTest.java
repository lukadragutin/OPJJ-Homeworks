package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ArrayIndexedCollectionTest {

	@Test
	void testAdd() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add(1);
		assertThrows(NullPointerException.class, () -> {
			i1.add(null);
		});
		assertTrue(i1.contains(1));
		assertEquals(1, i1.get(0));
	}
	
	@Test
	void testAddAll() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add(1);
		i1.add(2);
		i1.add("Šišmiš");
		ArrayIndexedCollection i2 = new ArrayIndexedCollection();
		i2.addAll(i1);
		assertTrue(i2.contains(1) && i2.contains(2) && i2.contains("Šišmiš")); 
	}
	
	@Test
	void testRemove1() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add(4.0);
		i1.add(true);
		i1.add("Štefica");
		assertTrue(i1.remove(true));
		assertFalse(i1.get(1).equals(true));
	}
	
	@Test
	void testGet() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add('a');
		assertThrows(IndexOutOfBoundsException.class, ()-> {
			i1.get(5);
		});
		assertEquals('a', i1.get(0));
	}
	
	@Test
	void testSize() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		assertEquals(0, i1.size());
		i1.add(34);
		i1.add(false);
		i1.add("Stop");
		assertEquals(3, i1.size());
	}
	
	@Test
	void testClear() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add(2223);
		i1.add("London");
		i1.add("Šišmiš");
		i1.clear();
		assertEquals(0, i1.size());
		assertThrows(IndexOutOfBoundsException.class, () -> {
			i1.get(1);
		});
		assertFalse(i1.contains(2223));
	}
	
	@Test
	void testIndexOf() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add(1);
		i1.add(2);
		i1.add("Šišmiš");
		assertEquals(1, i1.indexOf(2));
		assertEquals(-1, i1.indexOf(null));
	}
	
	void testForEach() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add(1);
		i1.add(2);
		i1.add("Šišmiš");
		i1.forEach(new Processor() {
			@Override
			public void process(Object value) {
				value = "Changed";
			}
		});
		assertEquals(true, i1.contains("Changed"));
		assertEquals("Changed", i1.get(2));
	}
	
	@Test
	void testInsert() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add(1);
		i1.add(2);
		i1.add("Šišmiš");
		i1.insert(3, 1);
		assertEquals(3, i1.get(1));
		assertEquals(4, i1.size());
		assertEquals("Šišmiš", i1.get(3));
	}
	
	@Test
	void testRemove2() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add(1);
		i1.add(2);
		i1.add("Šišmiš");
		i1.remove(1);
		assertEquals(2, i1.size());
		assertEquals("Šišmiš", i1.get(1));
	}
	
	@Test
	void testToArray() {
		ArrayIndexedCollection i1 = new ArrayIndexedCollection();
		i1.add(1);
		i1.add(2);
		i1.add("Šišmiš");
		Object[] o = i1.toArray();
		assertEquals(1, o[0]);
		assertEquals(2, o[1]);
		assertEquals("Šišmiš", o[2]);
		assertEquals(3, o.length);
	}
}
