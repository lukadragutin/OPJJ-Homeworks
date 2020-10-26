package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PrimListModelTest {
	
	@Test
	void onlyOneAtStart() {
		PrimListModel model = new PrimListModel();
		assertEquals(1, model.getSize());
		assertEquals(1, model.getElementAt(0));
	}
	
	@Test
	void nextCreatesNumbers() {
		PrimListModel model = new PrimListModel();
		assertEquals(1, model.getSize());
		model.next();
		assertEquals(2, model.getSize());
		model.next();
		assertEquals(3, model.getSize());
	}
	
	@Test
	void nextCreatesCorrectPrimes() {
		PrimListModel model = new PrimListModel();
		model.next();
		model.next();
		model.next();
		model.next();
		model.next();
		Integer[] correctValues = new Integer[] {1,2,3,5,7,11};
		for(int i = 0; i < model.getSize(); i++) {
			assertEquals(correctValues[i], model.getElementAt(i));
		}
	}
	
	@Test
	void getSize() {
		PrimListModel model = new PrimListModel();
		for(int i = 2; i < 50; i++) {
			model.next();
			assertEquals(i, model.getSize());
		}
	}

}
