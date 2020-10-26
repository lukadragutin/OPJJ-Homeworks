package hr.fer.zemris.java.custom.collections;

public class EmptyStackException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmptyStackException(String message) {
		super(message);
	}
	
	public EmptyStackException() {
		super();
	}
}