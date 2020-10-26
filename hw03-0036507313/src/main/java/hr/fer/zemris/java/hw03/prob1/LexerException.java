package hr.fer.zemris.java.hw03.prob1;

import hr.fer.zemris.java.custom.scripting.lexer.ScriptingLexer;

/**
 * Iznimka koja nastaje kad dode do problema u razredima {@link Lexer} i {@link ScriptingLexer}
 * @author Luka
 *
 */
public class LexerException extends RuntimeException {

	public LexerException() {
		super();
	}
	
	public LexerException(String message) {
		super(message);
	}
}
