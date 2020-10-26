package hr.fer.zemris.java.custom.collections;

/**
 * Sucelje za testiranje vrijednosti
 * @author Luka Dragutin
 *
 */
public interface Tester<T> {

	/**
	 * Testira je li T obj zadovoljava neki uvjet
	 * @param obj Testirani objekt
	 * @return boolean true ako zadovoljava, false inace
	 */
	boolean test(T obj);
}
