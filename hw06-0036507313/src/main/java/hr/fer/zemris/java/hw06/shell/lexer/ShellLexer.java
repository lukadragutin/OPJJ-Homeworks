package hr.fer.zemris.java.hw06.shell.lexer;

import java.util.Objects;

/**
 * Lexer za obradu argumenata u shell programu
 * @author Luka Dragutin
 *
 */
public class ShellLexer {

	/** Polje znakova od predane naredbe */
	private char[] data;

	/** Zadnji predani token */
	private ShellToken<String> token;

	/** Indeks trenutnog znaka */
	private int currentIndex;

	public ShellLexer(String arguments) {
		data = Objects.requireNonNull(arguments).trim().toCharArray();
	}

	/**
	 * @return Zadnji generirani token
	 */
	public ShellToken<String> getToken() {
		return token;
	}

	/**
	 * Generira i vraća novi token ako postoji.
	 * 
	 * @return Novo generirani token
	 */
	public ShellToken<String> nextToken() {
		generateToken();
		return token;
	}

	/**
	 * Pomoćna metoda za dohvat tokena
	 * 
	 * @throws IllegalArgumentException ako su argumenti krivo zadani
	 */
	private void generateToken() {
		if (currentIndex >= data.length) {
			token = new ShellToken<String>(null, ShellTokenType.EOF);
			return;
		}
		char ch = data[currentIndex++];
		while (Character.isWhitespace(ch) && currentIndex <= data.length) {
			ch = data[currentIndex++];
		}
		if (ch == '"') {
			generatePath2();
		} else {
			currentIndex--;
			generatePath1();
		}
	}

	/**
	 * Pomoćna metoda za dohvat odredišta bez navodnika
	 */
	private void generatePath1() {
		var sb = new StringBuilder();
		char ch = data[currentIndex++];
		while(!Character.isWhitespace(ch)) {
			sb.append(ch);
			if(currentIndex == data.length) {
				break;
			}
			ch = data[currentIndex++];
		}
		token = new ShellToken<String>(sb.toString(), ShellTokenType.PATH);
	}

	/**
	 * Pomoćna metoda za dohvat odredišta pod navodnicima
	 */
	private void generatePath2() {
		StringBuilder sb = new StringBuilder();
		char ch = data[currentIndex++];
		while (ch != '"') {
			if (ch == '\\' && currentIndex < data.length && (data[currentIndex] == '"' || data[currentIndex] == '\\')) {
				sb.append(data[currentIndex++]);
			} else {
				sb.append(ch);
			}
			if (currentIndex >= data.length) {
				throw new IllegalArgumentException("Path expression not closed!");
			}
			ch = data[currentIndex++];
		}
		if(currentIndex < data.length && !Character.isWhitespace(data[currentIndex])) {
			throw new IllegalArgumentException("Paths must be separated!");
		}
		token = new ShellToken<String>(sb.toString(), ShellTokenType.PATH);
	}
}
