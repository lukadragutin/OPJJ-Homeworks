package hr.fer.zemris.java.custom.collections;

/**
 * Neprovjeravana iznimka koja nastaje kad pokusamo
 * pristupiti praznom stogu
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class EmptyStackException extends RuntimeException {
	
	/**
	 * Ispisuje poruku uz bacenu iznimku
	 * @param message Poruka koja se ispise
	 */
	public EmptyStackException(String message) {
		super(message);
	}
	
	/**
	 * Defaultni konstruktor
	 */
	public EmptyStackException() {
		super();
	}
}