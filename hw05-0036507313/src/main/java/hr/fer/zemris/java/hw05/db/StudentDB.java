package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Program za upravljanje bazom podataka preko komandne linije.
 * @author Luka Dragutin
 *
 */
public class StudentDB {

	public static void main(String[] args) {

		StudentDatabase database = null;

		try {
			List<String> lines = Files.readAllLines(Paths.get("D:\\Downloads\\database.txt"), StandardCharsets.UTF_8);
			database = new StudentDatabase(lines);
		} catch (IOException e1) {
			System.out.println("Error while reading from database file.");
			System.exit(1);
		} catch (IllegalArgumentException e2) {
			System.out.println(e2.getMessage());
		}
		System.out.println("Database loaded. Enter your query.");
		Scanner sc = new Scanner(System.in);

		while (true) {
			try {
				System.out.print("> ");
				String query = sc.nextLine().trim();
				if (query.equals("exit")) {
					System.out.println("Goodbye!");
					sc.close();
					System.exit(0);
				}

				if (query.startsWith("query")) {
					QueryParser qp = new QueryParser(query.substring(5).trim());
					if (qp.isDirectQuery()) {
						var student = database.forJMBAG(qp.getQuerriedJMBAG());
						System.out.println("Using index for record retrieval.");
						if(student != null) {
							print(List.of(student));
						}
						else {
							print(new ArrayList<>());
						}
					} else {
						var filtered = database.filter(new QueryFilter(qp.getQuery()));
						print(filtered);
					}
				} else {
					System.out.println("Query must start with keyword 'query'.");
				}
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private static void print(List<StudentRecord> filtered) {
		if (filtered.isEmpty()) {
			System.out.printf("Records selected: %d\n", filtered.size());
			return;
		}
		int firstName = 0;
		int lastName = 0;
		for (var s : filtered) {
			if (s.getFirstName().length() > firstName) {
				firstName = s.getFirstName().length();
			}
			if (s.getLastName().length() > lastName) {
				lastName = s.getLastName().length();
			}
		}
		frame(firstName, lastName);
		for (var s : filtered) {
			System.out.printf("| %s |", s.getJmbag());
			System.out.printf(" %s ", s.getLastName());
			printMultiple(" ", lastName - s.getLastName().length());
			System.out.print("|");
			System.out.printf(" %s ", s.getFirstName());
			printMultiple(" ", firstName - s.getFirstName().length());
			System.out.printf("| %d |\n", s.getFinalGrade());
		}
		frame(firstName, lastName);
		System.out.printf("Records selected: %d\n", filtered.size());
	}

	private static void frame(int firstName, int lastName) {
		System.out.print("+============+");
		printMultiple("=", lastName + 2);
		System.out.print("+");
		printMultiple("=", firstName + 2);
		System.out.print("+===+\n");

	}

	private static void printMultiple(String string, int repeat) {
		for (int i = 1; i <= repeat; i++) {
			System.out.print(string);
		}

	}

}
