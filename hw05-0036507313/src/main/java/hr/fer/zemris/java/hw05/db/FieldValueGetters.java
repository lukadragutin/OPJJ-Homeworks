package hr.fer.zemris.java.hw05.db;

/**
 * Dohvaćaju jednu od članskih varijabli objekta {@link StudentRecord}
 * @author Luka Dragutin
 */
public class FieldValueGetters {

	/**
	 * Dohvaca člansku varijablu {@code firstName} objekta {@link StudentRecord}
	 */
	public static final IFieldValueGetter FIRST_NAME = (r) -> r.getFirstName();
	
	/**
	 * Dohvaca člansku varijablu {@code lastName} objekta {@link StudentRecord}
	 */
	public static final IFieldValueGetter LAST_NAME = (r) -> r.getLastName();
	
	/**
	 * Dohvaca člansku varijablu {@code jmbag} objekta {@link StudentRecord}
	 */
	public static final IFieldValueGetter JMBAG = (r) -> r.getJmbag();

}
