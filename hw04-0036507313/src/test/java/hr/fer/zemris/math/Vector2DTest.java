package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Vector2DTest {

	private final static double DELTA = 10E-6;
	@Test
	void constructor() {
		Vector2D v1 = new Vector2D(2, 2);
		Vector2D v2 = new Vector2D(2, 2);
		Vector2D v3 = new Vector2D(3, 3);
		assertTrue(v1.equals(v2));
		assertFalse(v1.equals(v3));
		assertEquals(2, v1.getX());
		assertEquals(2, v1.getY());
	}
	
	@Test
	void translate() {
		Vector2D v1 = new Vector2D(2, 2);
		Vector2D v2 = new Vector2D(3, 3);
		v1.translate(v2);
		assertEquals(new Vector2D(5,5), v1);
		assertEquals(5, v1.getX());
		assertEquals(5, v1.getY());
	}
	
	@Test
	void translateNull() {
		Vector2D v1 = new Vector2D(2, 3);
		assertThrows(NullPointerException.class, () -> v1.translate(null));
	}
	
	@Test
	void translated() {
		Vector2D v1 = new Vector2D(2, 2);
		Vector2D v2 = new Vector2D(2, 2);
		Vector2D v3 = v1.translated(v2);
		assertEquals(new Vector2D(4,4), v3);
		assertEquals(4, v3.getX());
		assertEquals(4, v3.getY());
	}
	
	@Test
	void translatedNull() {
		Vector2D v1 = new Vector2D(2, 3);
		assertThrows(NullPointerException.class, () -> v1.translated(null));
	}
	
	@Test
	void rotate() {
		Vector2D v1 = new Vector2D(2, 2);
		v1.rotate(Math.PI);
		assertEquals(-2,v1.getX(), DELTA);
		assertEquals(-2,v1.getY(), DELTA);
		v1.rotate(Math.PI/2);
		assertEquals(2, v1.getX(), DELTA);
		assertEquals(-2, v1.getY(), DELTA);
	}
	
	@Test
	void rotated() {
		Vector2D v1 = new Vector2D(2, 2);
		Vector2D v2 = v1.rotated(Math.PI);
		assertEquals(-2,v2.getX(), DELTA);
		assertEquals(-2,v2.getY(), DELTA);
		Vector2D v3 = v2.rotated(Math.PI/2);
		assertEquals(2, v3.getX(), DELTA);
		assertEquals(-2, v3.getY(), DELTA);
	}
	
	@Test
	void scale() {
		Vector2D v1 = new Vector2D(2, 2);
		v1.scale(10);
		assertEquals(20, v1.getX());
		assertEquals(20, v1.getY());
		v1.scale(0.01);
		assertEquals(0.2, v1.getX());
		assertEquals(0.2, v1.getY());
	}
	
	@Test
	void scaled() {
		Vector2D v1 = new Vector2D(2, 2);
		Vector2D v2 = v1.scaled(10);
		assertEquals(20, v2.getX());
		assertEquals(20, v2.getY());
		Vector2D v3 = v2.scaled(0.01);
		assertEquals(0.2, v3.getX());
		assertEquals(0.2, v3.getY());
	}
	
	@Test
	void copy() {
		Vector2D v1 = new Vector2D(2, 2);
		Vector2D v2 = v1.copy();
		assertTrue(v1.equals(v2));
		assertEquals(2, v2.getX());
		assertEquals(2, v2.getY());
	}
	
	@Test
	void getX() {
		Vector2D v1 = new Vector2D(2.2, 5.1);
		Vector2D v2 = new Vector2D(4, 2.9);
		Vector2D v3 = new Vector2D(-12, -1);
		assertEquals(2.2, v1.getX());
		assertEquals(4, v2.getX());
		assertEquals(-12, v3.getX());
	}
	
	@Test
	void getY() {
		Vector2D v1 = new Vector2D(2.2, 21);
		Vector2D v2 = new Vector2D(4, 0);
		Vector2D v3 = new Vector2D(-12, -2.3);
		assertEquals(21, v1.getY());
		assertEquals(0, v2.getY());
		assertEquals(-2.3, v3.getY());
	}
}
