package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * Pomoćni lexer za dohvaćanje zapisa iz naredbi query
 * 
 * @author Luka Dragutin
 */
public class QueryLexer {

	/** Polje znakova od predane naredbe */
	private char[] data;

	/** Zadnji predani token */
	private Token<String> token;

	/** Indeks trenutnog znaka */
	private int currentIndex;

	public QueryLexer(String query) {
		data = Objects.requireNonNull(query).trim().toCharArray();
	}

	public Token<String> getToken() {
		return token;
	}

	/**
	 * Generira i vraća novi token ako postoji.
	 * 
	 * @return Novo generirani token
	 */
	public Token<String> nextToken() {
		generateToken();
		return token;
	}

	/**
	 * Pomoćna metoda za dohvat tokena
	 * 
	 * @throws IllegalArgumentException ako je zapis krivo struktruriran
	 */
	private void generateToken() {
		if (currentIndex >= data.length) {
			token = new Token<String>(null, TokenType.END);
			return;
		}
		char ch = data[currentIndex++];
		while (Character.isWhitespace(ch) && currentIndex <= data.length) {
			ch = data[currentIndex++];
		}
		currentIndex--;
		if (Character.isLetter(ch) || ch == '"') {
			generateWord();
		} else if (isOperator(ch)) {
			generateOperator();
		} else {
			throw new IllegalArgumentException("Wrong query format!");
		}
	}

	/**
	 * Generira token tipa {@link TokenType.OPERATOR}
	 * 
	 * @throws IllegalArgumentException ako operator nije ispravno zadan
	 */
	private void generateOperator() {
		char ch = data[currentIndex++];
		if ((ch == '<' || ch == '>') && currentIndex < data.length && data[currentIndex] == '=') {
			token = new Token<String>(String.valueOf(ch) + String.valueOf(data[currentIndex++]), TokenType.OPERATOR);
			return;
		} else if (ch == '!') {
			if (currentIndex < data.length && data[currentIndex] == '=') {
				token = new Token<String>(String.valueOf(ch) + String.valueOf(data[currentIndex++]),
						TokenType.OPERATOR);
				return;
			} else {
				throw new IllegalArgumentException("Wrong query format!");
			}
		} else {
			token = new Token<String>(String.valueOf(ch), TokenType.OPERATOR);
		}

	}

	/**
	 * Pomoćna metoda koja provjerava je li znak jedan od dozvoljenih operatora
	 * 
	 * @param ch znak koji se provjerava
	 * @return <code>true</code> ako je dozvoljen, <code>false</code> inače
	 */
	private boolean isOperator(char ch) {
		if (ch == '<' || ch == '>' || ch == '=' || ch == '!') {
			return true;
		}
		return false;
	}

	/**
	 * Pomoćna metoda za generiranje idućeg tokena koji je ili clanska varijabla ili
	 * String literal
	 * 
	 * @throws IllegalArgumentException ako je krivo strukturirana naredba
	 */
	private void generateWord() {
		StringBuilder sb = new StringBuilder();
		boolean isString = false;
		char ch = data[currentIndex++];
		if (ch == '"') {
			ch = data[currentIndex++];
			isString = true;
		}
		while ((Character.isLetterOrDigit(ch) || ch == '*') && ch != '"') {
			sb.append(ch);
			if (currentIndex == data.length) {
				if (isString) {
					throw new IllegalArgumentException("String literal not closed!");
				}
				currentIndex++;
				break;
			}
			ch = data[currentIndex++];
		}
		currentIndex--;
		if (isString) {
			token = new Token<String>(sb.toString(), TokenType.STRING);
			currentIndex++;
			return;
		} else if (sb.toString().equalsIgnoreCase("and")) {
			token = new Token<String>(null, TokenType.AND);
		} else if (sb.toString().equals("LIKE")) {
			token = new Token<String>(sb.toString(), TokenType.OPERATOR);
		} else {
			token = new Token<String>(sb.toString(), TokenType.FIELDNAME);
		}
	}
}
