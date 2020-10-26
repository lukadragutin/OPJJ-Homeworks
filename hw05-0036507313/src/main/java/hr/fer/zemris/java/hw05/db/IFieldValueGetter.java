package hr.fer.zemris.java.hw05.db;

/**
 * Sucelje koje modelira dohvaćanje
 * vrijednosti clanskih varijabli objekta {@link StudentRecord}
 * @author Luka Dragutin
 *
 */
public interface IFieldValueGetter {

	/**
	 * Metoda kojom dohvaćamo traženu varijablu objekta {@link StudentRecord}
	 * @param record Primjerak objekta od kojeg dobivamo vrijednost varijable
	 * @return Vrijednost tražene članske varijable objekta {@link StudentRecord}
	 */
	public String get(StudentRecord record);
}
