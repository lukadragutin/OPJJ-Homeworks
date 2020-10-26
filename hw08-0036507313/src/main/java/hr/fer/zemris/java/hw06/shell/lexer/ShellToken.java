package hr.fer.zemris.java.hw06.shell.lexer;

import java.util.Objects;

/**
 * Pomoćni tip podataka za obradu argumenata iz Shella
 * @author Luka Dragutin
 *
 * @param <T> Tip vrijednosti tokena
 */
public class ShellToken<T> {

	/** Vrijednost tokena */
	private T value;

	/** Tip tokena */
	private ShellTokenType type;

	
	public ShellToken(T value, ShellTokenType type) {
		this.value = value;
		this.type = Objects.requireNonNull(type);
	}

	/**
	 * @return vrijednost tokena
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @return tip tokena
	 */
	public ShellTokenType getType() {
		return type;
	}

}
