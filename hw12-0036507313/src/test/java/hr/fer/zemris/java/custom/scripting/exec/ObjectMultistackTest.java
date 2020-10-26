package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EmptyStackException;

import org.junit.jupiter.api.Test;

class ObjectMultistackTest {

	@Test
	void pushPop() {
		var stack = new ObjectMultistack();
		stack.push("Vrijeme", new ValueWrapper("Suncano"));
		stack.push("Vrijeme", new ValueWrapper("Oblacno"));
		assertFalse(stack.isEmpty("Vrijeme"));
		assertEquals("Oblacno", stack.pop("Vrijeme").getValue());
		assertEquals("Suncano", stack.pop("Vrijeme").getValue());
	}

	@Test
	void peek() {
		var stack = new ObjectMultistack();
		stack.push("Vrijeme", new ValueWrapper("Vjetrovito"));
		assertEquals("Vjetrovito", stack.peek("Vrijeme").getValue());
		assertFalse(stack.isEmpty("Vrijeme"));
	}
	
	@Test
	void isEmpty() {
		var stack = new ObjectMultistack();
		stack.push("Vrijeme", new ValueWrapper("Vjetrovito"));
		assertFalse(stack.isEmpty("Vrijeme"));
		stack.pop("Vrijeme");
		assertTrue(stack.isEmpty("Vrijeme"));
		assertTrue(stack.isEmpty("Prazno"));
	}
	
	@Test
	void emptyStack() {
		var stack = new ObjectMultistack();
		assertThrows(EmptyStackException.class, () -> stack.pop("Exception"));
		assertThrows(EmptyStackException.class, () -> stack.peek("Exception"));
	}
	
}
