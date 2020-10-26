package hr.fer.zemris.java.custom.collections;

/**
 * Sucelje za testiranje vrijednosti
 * @author Luka Dragutin
 *
 */
public interface Tester {

	/**
	 * Testira je li Object obj zadovoljava neki uvjet
	 * @param obj Testirani objekt
	 * @return boolean true ako zadovoljava, false inace
	 */
	boolean test(Object obj);
}
