package hr.fer.zemris.java.gui.layouts;


/**
 * Iznimka koja se baca kod koristenja
 * razreda {@link CalcLayout}
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class CalcLayoutException extends RuntimeException {
	
	public CalcLayoutException() {
		super();
	}
	
	public CalcLayoutException(String message) {
		super(message);
	}
	
	public CalcLayoutException(String message, Throwable cause) {
		super(message, cause);
	}
}
