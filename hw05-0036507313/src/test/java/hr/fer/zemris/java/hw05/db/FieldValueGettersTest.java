package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FieldValueGettersTest {

	@Test
	void test() {
		IFieldValueGetter jmbag = FieldValueGetters.JMBAG;
		IFieldValueGetter lastName = FieldValueGetters.LAST_NAME;
		IFieldValueGetter firstName = FieldValueGetters.FIRST_NAME;
		StudentRecord student = new StudentRecord("0036507313", "Dragutin", "Luka", 2);
		assertEquals("0036507313", jmbag.get(student));
		assertEquals("Luka", firstName.get(student));
		assertEquals("Dragutin", lastName.get(student));
	}

}
