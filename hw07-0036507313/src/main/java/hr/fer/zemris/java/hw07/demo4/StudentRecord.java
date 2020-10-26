package hr.fer.zemris.java.hw07.demo4;

public class StudentRecord {

	private String jmbag;
	private String prezime;
	private String ime;
	private double bodoviMI;
	private double bodoviZI;
	private double bodoviLAB;
	private int ocjena;

	public StudentRecord(String jmbag, String prezime, String ime, double bodoviMI, double bodoviZI, double bodoviLAB,
			int ocjena) {
		super();
		this.jmbag = jmbag;
		this.prezime = prezime;
		this.ime = ime;
		this.bodoviMI = bodoviMI;
		this.bodoviZI = bodoviZI;
		this.bodoviLAB = bodoviLAB;
		this.ocjena = ocjena;
	}

	public String getJmbag() {
		return jmbag;
	}

	public String getPrezime() {
		return prezime;
	}

	public String getIme() {
		return ime;
	}

	public double getBodoviMI() {
		return bodoviMI;
	}

	public double getBodoviZI() {
		return bodoviZI;
	}

	public double getBodoviLAB() {
		return bodoviLAB;
	}

	public int getOcjena() {
		return ocjena;
	}

	@Override
	public String toString() {
		return jmbag + "\t" + prezime + "\t" + ime + "\t" + bodoviMI + "\t" + bodoviZI + "\t" + bodoviLAB + "\t"
				+ ocjena;
	}

}
