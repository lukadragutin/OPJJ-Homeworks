package hr.fer.zemris.java.hw06.shell;

@SuppressWarnings("serial")
/**
 * Iznimka koja nastaje pri ispisu na izlaz
 * ili čitanju sa ulaza okruženja MyShell
 * @author Luka Dragutin
 *
 */
public class ShellIOException extends RuntimeException {

	public ShellIOException(String message) {
		super(message);
	}
	
	public ShellIOException() {
		super();
	}
	
	
}
