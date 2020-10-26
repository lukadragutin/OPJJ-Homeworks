package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * Modelira token koji koristi {@link Lexer} za parsiranje teksta
 * @author Luka Dragutin
 *
 */
public class Token {

	/**
	 * Tip tokena iz enuma {@link TokenType}
	 */
	private TokenType type;
	
	/**
	 * Vrijednost tokena
	 */
	private Object value;
	

	/**
	 * @param type tip tokena
	 * @param value vrijednost tokena
	 * @throws NullPointerException ako je type null
	 */
	public Token(TokenType type, Object value) {
		super();
		this.type = Objects.requireNonNull(type);
		this.value = value;
	}

	/**
	 * @return Koji tip tokena iz enum TypeToken
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * @return Vrijednost pohranjena u tokenu
	 */
	public Object getValue() {
		return value;
	}
}
