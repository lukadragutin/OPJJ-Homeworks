package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FactorialTest extends Factorial {
	
	/**
	 * Tests if the factorial calculator
	 * gives the correct result
	 */
	@Test
	void calculateFactorialAccuracyTest() {
		int n = Factorial.calculateFactorial(8);
		assertEquals(40320, n);
		n = Factorial.calculateFactorial(3);
		assertEquals(6, n);
	}
	
	/**
	 * Tests if the factorial calculator throws
	 * the correct exception when given a parameter
	 * out of range 
	 */
	@Test
	void rangeTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			Factorial.calculateFactorial(1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Factorial.calculateFactorial(24);
		});
	}

}
