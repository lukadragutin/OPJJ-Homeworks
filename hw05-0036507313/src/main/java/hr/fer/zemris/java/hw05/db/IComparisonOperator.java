package hr.fer.zemris.java.hw05.db;

/**
 * Sucelje koje modelira strategiju uspoređivanja
 * dvaju String vrijednosti.
 * @author Luka Dragutin
 */
public interface IComparisonOperator {

	/**
	 * Metoda koja vrši provjeru zadovoljavaju li predani
	 * argumenti operator usporedbe
	 * @param value1 Argument s lijeve strane operatora usporedbe
	 * @param value2 Argument s desne strane operatora usporedbe
	 * @return {@code true}, ako je zadovoljena usporedba,
	 * {@code false} ako nije.
	 */
	public boolean satisfied(String value1, String value2);
}
