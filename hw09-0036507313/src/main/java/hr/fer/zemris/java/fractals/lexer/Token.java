package hr.fer.zemris.java.fractals.lexer;

import java.util.Objects;

/**
 * Najmanja smislena cjelina lexera
 * @author Luka Dragutin
 *
 */
public class Token {

	/** Vrijednost tokena */
	private String value;
	
	/** Tip tokena */
	private TokenType type;
	
	public Token(String value, TokenType type) {
		this.value = value;
		this.type = Objects.requireNonNull(type);
	}

	/** Vraca vrijednost tokena  */
	public String getValue() {
		return value;
	}

	/** Vraca tip tokena */
	public TokenType getType() {
		return type;
	}
}
