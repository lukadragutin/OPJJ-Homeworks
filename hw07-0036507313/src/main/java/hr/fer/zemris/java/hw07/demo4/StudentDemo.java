package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDemo {

	public static void main(String[] args) {

		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("./studenti.txt"));
		} catch (IOException e) {
			System.out.println("Error reading file!");
			System.exit(1);
		}
		List<StudentRecord> records = convert(lines);
		printZadatak(1);
		System.out.println(vratiBodovaViseOd25(records));

		printZadatak(2);
		System.out.println(vratiBrojOdlikasa(records));

		printZadatak(3);
		vratiListuOdlikasa(records).forEach(System.out::println);
		;

		printZadatak(4);
		vratiSortiranuListuOdlikasa(records).forEach(System.out::println);

		printZadatak(5);
		vratiPopisNepolozenih(records).forEach(System.out::println);

		printZadatak(6);
		razvrstajStudentePoOcjenama(records).forEach((k, v) -> {
			System.out.println(k.intValue() + ": ");
			v.forEach(System.out::println);
			;
		});

		printZadatak(7);
		vratiBrojStudenataPoOcjenama(records)
				.forEach((k, v) -> System.out.println(k.intValue() + " --> " + v.intValue()));

		printZadatak(8);
		razvrstajProlazPad(records).forEach((k, v) -> {
			System.out.println(k.toString() + ":");
			v.forEach(System.out::println);
		});
	}

	private static void printZadatak(int zadatak) {
		System.out.println("Zadatak " + zadatak + "\n=========");
	}

	private static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream().filter(e -> (e.getBodoviLAB() + e.getBodoviMI() + e.getBodoviZI()) > 25).count();
	}

	private static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(e -> e.getOcjena() == 5).count();
	}

	private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(e -> e.getOcjena() == 5).collect(Collectors.toList());
	}

	private static List<StudentRecord> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream().filter(e -> e.getOcjena() == 1)
				.sorted((e1, e2) -> e1.getJmbag().compareTo(e2.getJmbag())).collect(Collectors.toList());
	}

	private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(e -> e.getOcjena() == 5)
				.sorted((e1,
						e2) -> (Double.valueOf(e2.getBodoviLAB() + e2.getBodoviMI() + e2.getBodoviZI())
								.compareTo(Double.valueOf(e1.getBodoviLAB() + e1.getBodoviMI() + e1.getBodoviZI()))))
				.collect(Collectors.toList());
	}

	private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		return records.stream().collect(Collectors.groupingBy(StudentRecord::getOcjena));
	}

	private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		return records.stream().collect(Collectors.toMap(StudentRecord::getOcjena, e -> 1, (e, v) -> e + 1));
	}

	private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		return records.stream().collect(Collectors.partitioningBy(e -> e.getOcjena() > 1));
	}

	private static List<StudentRecord> convert(List<String> lines) {
		ArrayList<StudentRecord> records = new ArrayList<>();
		for (var s : lines) {
			if (s.isBlank())
				continue;
			records.add(extractRecord(s));
		}
		return records;
	}

	private static StudentRecord extractRecord(String s) {
		var split = s.split("\t");
		if (split.length != 7) {
			throw new RuntimeException("Wrong data format!");
		}
		StudentRecord record;
		try {
			record = new StudentRecord(split[0], split[1], split[2], Double.parseDouble(split[3]),
					Double.parseDouble(split[4]), Double.parseDouble(split[5]), Integer.parseInt(split[6]));
		} catch (NumberFormatException e) {
			throw new RuntimeException("Wrong data format!");
		}
		return record;
	}

}
