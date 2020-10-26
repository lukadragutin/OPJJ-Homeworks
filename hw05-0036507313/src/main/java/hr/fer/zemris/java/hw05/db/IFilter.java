package hr.fer.zemris.java.hw05.db;

/**
 * Sucelje koje modelira strategiju filtiranja
 * objekata {@link StudentRecord} tako da ispituje zadovoljavaju
 * li predani primjerci zadane uvjete
 * @author Luka Dragutin
 */
public interface IFilter {

	/**
	 * Metoda koja testira jedan primjerak razreda {@link StudentRecord}
	 * ako zadovoljava tražene uvjete
	 * @param record Testirani primjerak
	 * @return {@code true} ako je uvjet zadovoljen, {@code false} inače.
	 */
	public boolean accepts(StudentRecord record);
}
