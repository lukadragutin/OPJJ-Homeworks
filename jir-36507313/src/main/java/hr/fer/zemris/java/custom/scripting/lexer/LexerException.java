package hr.fer.zemris.java.custom.scripting.lexer;

@SuppressWarnings("serial")
public class LexerException extends RuntimeException {

	public LexerException() {
		super();
	}
	
	public LexerException(String message) {
		super(message);
	}
	
	public LexerException(Throwable cause) {
		super(cause);
	}
	
	public LexerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
	
}
