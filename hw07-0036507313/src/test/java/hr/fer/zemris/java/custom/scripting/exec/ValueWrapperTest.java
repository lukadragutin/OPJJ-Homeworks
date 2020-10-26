package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValueWrapperTest {

	@Test
	void add() {
		var v1 = new ValueWrapper(3);
		v1.add("7");
		assertEquals(Integer.valueOf(10), v1.getValue());
		var v2 = new ValueWrapper(2.3);
		v1.add(v2.getValue());
		assertEquals(Double.valueOf(2.3), v2.getValue());
		assertEquals(Double.valueOf(12.3), v1.getValue());
		v1.add(null);
		assertEquals(Double.valueOf(12.3), v1.getValue());
	}
	
	@Test
	void subtract() {
		var v1 = new ValueWrapper(3);
		v1.subtract("7");
		assertEquals(Integer.valueOf(-4), v1.getValue());
		var v2 = new ValueWrapper(2.3);
		v1.subtract(v2.getValue());
		assertEquals(Double.valueOf(2.3), v2.getValue());
		assertEquals(Double.valueOf(-6.3), v1.getValue());
		v1.subtract(null);
		assertEquals(Double.valueOf(-6.3), v1.getValue());
	}
	
	@Test 
	void divide() {
		var v1 = new ValueWrapper(10);
		v1.divide("3");
		assertEquals(Integer.valueOf(3), v1.getValue());
		var v2 = new ValueWrapper(2.0);
		v1.divide(v2.getValue());
		assertEquals(Double.valueOf(2.0), v2.getValue());
		assertEquals(Double.valueOf(1.5), v1.getValue());
	}
	
	@Test
	void multiply () {
		var v1 = new ValueWrapper(10);
		v1.multiply("3");
		assertEquals(Integer.valueOf(30), v1.getValue());
		var v2 = new ValueWrapper(2.0);
		v1.multiply(v2.getValue());
		assertEquals(Double.valueOf(2.0), v2.getValue());
		assertEquals(Double.valueOf(60.0), v1.getValue());
		v1.multiply(null);
		assertEquals(Double.valueOf(0), v1.getValue());
	}
	
	@Test
	void wrongArguments() {
		var v1 = new ValueWrapper("Marko");
		var v2 = new ValueWrapper(new StringBuilder());
		var v3 = new ValueWrapper(null);
		var v4 = new ValueWrapper(3);
		
		assertThrows(RuntimeException.class, () -> v1.numCompare(v4.getValue()));
		assertThrows(RuntimeException.class, () -> v2.add(v4.getValue()));
		assertThrows(RuntimeException.class, () -> v3.divide(v1.getValue()));
		assertThrows(RuntimeException.class, () -> v4.multiply(v2.getValue()));

	}
	
	@Test
	void numCompare() {
		var v1 = new ValueWrapper(10);
		var v2 = new ValueWrapper("1");
		var v3 = new ValueWrapper("1.2E1");
		var v4 = new ValueWrapper(null);
		var v5 = new ValueWrapper("Ante");
		var v6 = new ValueWrapper(12);
		assertTrue(v1.numCompare(v2.getValue()) > 0);
		assertTrue(v3.numCompare(v1.getValue()) > 0);
		assertTrue(v4.numCompare(v3.getValue()) < 0);
		assertTrue(v1.numCompare(v6.getValue()) < 0);
		assertTrue(v3.numCompare(v6.getValue()) == 0);
		assertTrue(v2.numCompare(v4.getValue()) > 0);
		assertThrows(RuntimeException.class, () -> v5.numCompare(v2.getValue()));
	}

}
