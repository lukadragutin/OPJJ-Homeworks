package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementira bazu podataka studenata.
 * Pohranjuje studente u obliku liste podataka, ali 
 * i čuva mapu studenata koji su indeksirani sa jmbagom
 * za brži dohvat studenata ako im znamo jmbag.
 * @author Luka Dragutin
 */
public class StudentDatabase {

	/**Lista studenata*/
	private List<StudentRecord> students;
	
	/**Mapa studenata za brze dohvaćanje sa jmbagom*/
	private Map<String, StudentRecord> studentMap;

	/**
	 * Stvara bazu podataka studenata iz liste Stringova zapisa
	 * podataka o studentima gdje svaki zapis sadrži:
	 * jmbag, ime, prezime i završnu ocjenu.
	 * @param database Lista zapisa o studentima
	 * @throws IllegalArgumentException ako se ponovi isti jmbag
	 * ili ako je ocjena izvan dosega ocjena [1, 5]
	 */
	public StudentDatabase(List<String> database) {
		students = new ArrayList<>();
		studentMap = new HashMap<>();
		for (String s : database) {
			var record = getRecord(s);
			if(studentMap.containsKey(record.getJmbag())) {
				throw new IllegalArgumentException("That jmbag is already in use!");
			}
			if(record.getFinalGrade() > 5 || record.getFinalGrade() < 1) {
				throw new IllegalArgumentException("Wrong grade value!"); 
			}
			students.add(record);
			studentMap.put(record.getJmbag(), record);
		}
	}
	
	/**
	 * Vraća kopiju liste studenata
	 */
	public List<StudentRecord> getStudentsCopy() {
		return new ArrayList<>(students);
	}

	/**
	 * Dohvaća studenta sa traženim jmbag-om.
	 * Složenosti O(1).
	 * @param jmbag traženog studenta
	 * @return Studenta sa predanim jmbagom ako postoji,
	 * a <code>null</code> ako ga nema.
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return studentMap.get(Objects.requireNonNull(jmbag));
	}
	
	/**
	 * Vraća filtriranu listu studenata iz baze podataka
	 * na osnovu predanog filtera
	 * @param filter na osnovu kojeg se biraju studenti koji zadovoljavaju uvjete
	 * @return Lista studenata koji zadovoljavaju filter
	 * @throws NullPointerException ako su filteri <code>null</code>
	 */
	public List<StudentRecord> filter(IFilter filter) {
		Objects.requireNonNull(filter);
		List<StudentRecord> filtered = new ArrayList<>();
		for(StudentRecord sr : students) {
			if(filter.accepts(sr)) {
				filtered.add(sr);
			}
		}
		return filtered;
	}

	/**
	 * Stvara i vraća jedan zapis o studentu iz Stringa u 
	 * kao primjerak objekta {@link StudentRecord}
	 * @param student String zapis o studentu
	 * @return podatke o studentu u primjerku {@link StudentRecord}
	 * @throws IllegalArgumentException ako je zapis netočno formatiran
	 */
	private StudentRecord getRecord(String student) {
		String[] values = Objects.requireNonNull(student).split("\t");
		if(values.length < 4) {
			throw new IllegalArgumentException("Wrong number of student parameters!");
		}
		try {
			return new StudentRecord(values[0], values[1], values[2], Integer.parseInt(values[3]));
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Wrong student information format!");
		}
	}
}
