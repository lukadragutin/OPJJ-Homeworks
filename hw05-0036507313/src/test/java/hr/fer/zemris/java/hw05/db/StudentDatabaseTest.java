package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentDatabaseTest {

	private StudentDatabase sd;
	private IFilter alwaysTrue = (s) -> true;
	private IFilter alwaysFalse = (s) -> false;

	@BeforeEach
	void setUp() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("D:\\Downloads\\database.txt"), StandardCharsets.UTF_8);
		sd = new StudentDatabase(lines);
	}

	@Test
	void forJMBAG() {
		StudentRecord sr = sd.forJMBAG("0000000005");
		assertEquals("Jusufadis", sr.getFirstName());
		assertEquals("Brezović", sr.getLastName());
		assertEquals(2, sr.getFinalGrade());
	}

	@Test
	void filterTrue() {
		var filtered = sd.filter(alwaysTrue);
		assertEquals(sd.getStudentsCopy(), filtered);
	}

	@Test
	void filterFalse() {
		var filtered = sd.filter(alwaysFalse);
		assertTrue(filtered.isEmpty());
	}
	
	@Test
	void wrongGrade() {
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(Arrays.asList("0000000005\tMate\tMatić\t9")));
	}
	
	@Test
	void doubleJMBAG() {
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(Arrays.asList("0000000005\tMate\tMatić\t4", "0000000005\tIvo\tIvić\t5")));
	}

}
