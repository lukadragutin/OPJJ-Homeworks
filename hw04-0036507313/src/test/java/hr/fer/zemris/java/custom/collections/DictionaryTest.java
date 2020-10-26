package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DictionaryTest {

	private Dictionary<Integer, String> setUpBeforeClass(){
		Dictionary<Integer, String> d = new Dictionary<>();
		d.put(23, "Štefica");
		d.put(10, "Marko");
		d.put(11, "Ivana");
		d.put(100, "Susjed");
		d.put(0, "Jasminka");
		d.put(111, "Lejla");
		d.put(7, "James Bond");
		d.put(5, "Slavoljub");
		return d;
	}

	@Test
	void get(){
		var d = setUpBeforeClass();
		var value = d.get(11);
		assertEquals("Ivana", value);
		value = d.get(0);
		assertEquals("Jasminka", value);
		value = d.get(10);
		assertEquals("Marko", value);
		value = d.get(1111);
		assertNull(value);
	}
	
	@Test
	void constructorWhenNullValue() {
		var d = new Dictionary<Integer, String>();
		assertDoesNotThrow(() -> d.put(22, null));
	}
	
	@Test
	void constructorWhenNullKey() {
		var d = new Dictionary<Integer, String>();
		assertThrows(NullPointerException.class, () -> d.put(null, "String"));
	}
	
	@Test
	void put() {
		var d = setUpBeforeClass();
		d.put(10, "Nije Marko");
		assertEquals("Nije Marko", d.get(10));
		d.put(10, "Opet Marko");
		assertEquals("Opet Marko", d.get(10));
		d.put(7, "Batman");
		assertEquals("Batman", d.get(7));
	}
	
	@Test
	void size() {
		var d = setUpBeforeClass();
		assertEquals(8, d.size());
		d.put(12, "Apostola");
		assertEquals(9, d.size());
		d.put(12, "dvanaest");
		assertEquals(9, d.size());
		d.clear();
		assertEquals(0, d.size());
	}
	
	@Test
	void clear() {
		var d = setUpBeforeClass();
		d.put(32, "Ivano");
		d.clear();
		assertNull(d.get(32));
		assertEquals(0, d.size());
		assertTrue(d.isEmpty());
	}
	
	@Test
	void isEmpty() {
		var d = new Dictionary<Integer, String>();
		assertTrue(d.isEmpty());
		d.put(21, "Jump Street");
		assertFalse(d.isEmpty());
		d.clear();
		assertTrue(d.isEmpty());
	}

}
