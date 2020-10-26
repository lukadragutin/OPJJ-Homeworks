package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueryFilterTest {

	private QueryParser qp;
	private StudentDatabase sd;
	
	@BeforeEach
	void setUp() throws IOException {
		qp = new QueryParser("jmbag > \"0000000058\" aNd firstName LIKE \"M*\"");
		List<String> lines = Files.readAllLines(Paths.get("D:\\Downloads\\database.txt"), StandardCharsets.UTF_8);
		sd = new StudentDatabase(lines);
	}

	@Test
	void test() {
		var filter = new QueryFilter(qp.getQuery());
		var filtered = sd.filter(filter);
		var student = filtered.get(0);
		assertEquals(1, filtered.size());
		assertEquals("Marin", student.getFirstName());
		assertEquals("0000000059", student.getJmbag());
	}

}
