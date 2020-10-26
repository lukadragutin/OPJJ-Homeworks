package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Razred koji bilježi podatke o studentu.
 * @author Luka Dragutin
 */
public class StudentRecord {

	/**
	 * Jedinstveni maticni broj studenta
	 */
	private String jmbag;
	
	/**Prezime studenta*/
	private String lastName;

	/**Ime studenta*/
	private String firstName;
	
	/**Ukupna ocjena studenta*/
	private int finalGrade;
	
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		super();
		this.jmbag = Objects.requireNonNull(jmbag);
		this.lastName = Objects.requireNonNull(lastName);
		this.firstName = Objects.requireNonNull(firstName);
		this.finalGrade = finalGrade;
	}
	

	public String getJmbag() {
		return jmbag;
	}

	
	public String getLastName() {
		return lastName;
	}


	public String getFirstName() {
		return firstName;
	}


	public int getFinalGrade() {
		return finalGrade;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StudentRecord))
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}
}
