package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * Razred za modeliranje izraza vraćenih preko {@link QueryLexer}
 * 
 * @author Luka Dragutin
 * @param <T> Tip vraćene vrijednosti
 */
public class Token<T> {

	/** Vrijednost tokena */
	private T value;

	/** Tip tokena */
	private TokenType type;

	public Token(T value, TokenType type) {
		this.value = value;
		this.type = Objects.requireNonNull(type);
	}

	public T getValue() {
		return value;
	}

	public TokenType getType() {
		return type;
	}
}