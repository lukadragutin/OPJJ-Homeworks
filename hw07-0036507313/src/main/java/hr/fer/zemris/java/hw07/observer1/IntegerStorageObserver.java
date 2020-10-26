package hr.fer.zemris.java.hw07.observer1;


/**
 * Sucelje koje javlja promjene nad {@link IntegerStorage}
 * @author Luka
 *
 */
public interface IntegerStorageObserver {

	
	/**
	 * 
	 * Metoda koja po promjeni pracenog objekta, obavlja svoj zadatak
	 * @param istorage
	 */
	public void valueChanged(IntegerStorage istorage);
}
