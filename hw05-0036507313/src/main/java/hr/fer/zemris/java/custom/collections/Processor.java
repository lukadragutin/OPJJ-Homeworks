package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koje definira "obris" objekta pomoću čije metode
 * {@link process} ćemo preko metode forEach prisutne u kolekcijama
 * moći jednom naredbom obraditi velike količine podataka 
 * @author Luka
 *
 */
public interface Processor<T> {
	
	/**
	 * Definira postupak koji se provodi nad predanim objektom
	 * @param value Vrijednost nad kojom se vrši procesiranje
	 */
	public void process(T value);
}
