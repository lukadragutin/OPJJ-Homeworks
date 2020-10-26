package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

class ComplexNumberTest {

	@Test
	void testGetReal(){
		ComplexNumber c1 = new ComplexNumber(1, 1);
		ComplexNumber c2 = new ComplexNumber(1, 4);
		ComplexNumber c3 = new ComplexNumber(2, 1);
		assertTrue(c1.getReal() == c2.getReal());
		assertFalse(c1.getReal() == c3.getReal());
	}
	
	void testGetImaginary(){
		ComplexNumber c1 = new ComplexNumber(1, 1);
		ComplexNumber c2 = new ComplexNumber(1, 4);
		ComplexNumber c3 = new ComplexNumber(2, 1);
		assertTrue(c1.getImaginary() == c3.getImaginary());
		assertFalse(c1.getImaginary() == c2.getImaginary());
	}
	
	@Test
	void testFromMagnitudeAndAngle() {
		ComplexNumber c1 = ComplexNumber.fromMagnitudeAndAngle(2, Math.PI/2);
		if((Math.abs(c1.getReal() - 0) > 10E-06) || 
				(Math.abs(c1.getImaginary() - 2) > 10E-06)) {
			fail("Method fromMagnitudeAndAngle is wrong.");
		}
	}
	
	@Test
	void testGetAngle1() {
		ComplexNumber c1 = new ComplexNumber(-1,-1);
		double angle = c1.getAngle();
		if(Math.abs((5/4.0)*Math.PI - angle) > 10E-06) {
			fail();
		};
	}
	
	@Test
	void testGetAngle2() {
		ComplexNumber c1 = new ComplexNumber(0,-1);
		double angle = c1.getAngle();
		assertEquals((3/2.0)*Math.PI, angle);
	}
	
	@Test
	void testParse1() {
		ComplexNumber c1 = ComplexNumber.parse("i");
		assertEquals(1, c1.getImaginary());
		assertEquals(0, c1.getReal());
	}
	
	@Test
	void testParse2() {
		ComplexNumber c1 = ComplexNumber.parse("+24");
		assertEquals(0, c1.getImaginary());
		assertEquals(24, c1.getReal());
	}
	
	@Test
	void testParse3() {
		ComplexNumber c1 = ComplexNumber.parse("14.5+i");
		assertEquals(1, c1.getImaginary());
		assertEquals(14.5, c1.getReal());
	}
	
	@Test
	void testParse4() {
		ComplexNumber c1 = ComplexNumber.parse("-142.345-34.65i");
		assertEquals(-34.65, c1.getImaginary());
		assertEquals(-142.345, c1.getReal());
	}
	@Test
	void testAdd() {
		ComplexNumber c1 = new ComplexNumber(1,1);
		ComplexNumber c2 = c1.add(new ComplexNumber(1, 1));
		assertEquals(2, c2.getReal());
		assertEquals(2, c2.getImaginary());
	}
	
	@Test
	void testSub() {
		ComplexNumber c1 = new ComplexNumber(2,2);
		ComplexNumber c2 = c1.sub(new ComplexNumber(1, 1));
		assertEquals(1.0, c2.getReal());
		assertEquals(1.0, c2.getImaginary());
	}
	
	@Test
	void testMul() {
		ComplexNumber c1 = new ComplexNumber(2,2);
		ComplexNumber c2 = c1.mul(new ComplexNumber(1, 1));
		assertEquals(0.0, c2.getReal());
		assertEquals(4.0, c2.getImaginary());
	}
	
	@Test
	void testDiv() {
		ComplexNumber c1 = new ComplexNumber(3,2);
		ComplexNumber c2 = c1.div(new ComplexNumber(2, 1));
		assertEquals(8/5.0, c2.getReal());
		assertEquals(1/5.0, c2.getImaginary());
	}
	
	@Test
	void testPower() {
		ComplexNumber c1 = new ComplexNumber(1,3);
		ComplexNumber c2 = c1.power(2);
		assertTrue(Math.abs(-8.0 - c2.getReal()) < 10E-06);
		assertTrue(Math.abs(6.0 - c2.getImaginary()) < 10E-06);
	}
	
	@Test
	void testRoot() {
		ComplexNumber c1 = new ComplexNumber(2,2);
		ComplexNumber[] c2 = c1.root(3);
		assertTrue(Math.abs(-1 - c2[1].getReal()) < 10E-06);
		assertTrue(Math.abs(1 - c2[1].getImaginary()) < 10E-06);
	}
	
	@Test
	void testEquals() {
		ComplexNumber c1 = new ComplexNumber(2.001, 11);
		ComplexNumber c2 = new ComplexNumber(2.001,11);
		ComplexNumber c3 = new ComplexNumber(2, 11);
		assertTrue(c1.equals(c2));
		assertFalse(c1.equals(c3));
	}
	@Test
	void testHashCode() {
		ComplexNumber c1 = new ComplexNumber(1, 5);
		ComplexNumber c2 = new ComplexNumber(1, 5);
		ComplexNumber c3 = new ComplexNumber(2, 5);
		assertTrue(c1.hashCode() == c2.hashCode());
		assertFalse(c1.hashCode() == c3.hashCode());
	}
}
