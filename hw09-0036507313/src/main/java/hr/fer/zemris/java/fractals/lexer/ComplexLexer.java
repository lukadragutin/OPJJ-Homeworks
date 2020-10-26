package hr.fer.zemris.java.fractals.lexer;

import java.util.ArrayList;

/**
 * Lexer za parsiranje kompleksnih brojeva
 * @author Luka Dragutin
 *
 */
public class ComplexLexer {

	/**Zadnja obradena vrijednost*/
	private Token token;
	
	/** Tekstualni zapis koji se parsira kao polje charova */
	private char[] data;
	
	/** Indeks trenutnog znaka koji se obraduje */
	private int currentIndex;

	public ComplexLexer(String line) {
		data = line.toCharArray();
	}

	/** Vraca generirani token  */
	public Token getToken() {
		return token;
	}

	/** Generira sljedeci token  */
	public Token nextToken() {
		generateToken();
		return token;
	}

	/** Pomocna metoda za generiranje sljedeceg tokena */
	private void generateToken() {
		if(currentIndex >= data.length) {
			token = new Token(null, TokenType.EOF);
			return;
		}
		
		while(Character.isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
		
		char ch = data[currentIndex];
		if(isOperator(ch)) {
			currentIndex++;
			return;
		}
		else if(ch == 'i') {
			token = new Token(null, TokenType.I);
			currentIndex++;
			return;
		}
		else if(Character.isDigit(ch)) {
			getNumber();
			return;
		}
		else {
			throw new RuntimeException("Not a valid complex number format!");
		}	
	}
	
	/** Vraca sve tokene u obliku liste */
	public ArrayList<Token> getTokenList() {
		generateToken();
		ArrayList<Token> tokenList = new ArrayList<>();
		while(!token.getType().equals(TokenType.EOF)) {
			tokenList.add(token);
			generateToken();
			}
		tokenList.add(token);
		return tokenList;
	}

	/** Pomocna metoda za generiranje iduceg tokena tipa {@link TokenType#NUMBER}
	 * @throws RuntimeException ako je predan netocan format kompleksnog broja */
	private void getNumber() {
		boolean decimalDot = false;
		StringBuilder sb = new StringBuilder();
		char ch = data[currentIndex];
		while (Character.isDigit(ch) || ch == '.' && !decimalDot) {
			if (ch == '.') {
				decimalDot = true;
			}
			sb.append(ch);
			++currentIndex;
			if (currentIndex >= data.length) {
				break;
			}
			ch = data[currentIndex];
		}
		if (ch == '.' && decimalDot) {
			throw new RuntimeException("Wrong decimal number format!");
		}
		token = new Token(sb.toString(), TokenType.NUMBER);
	}

	/** Provjerava je li znak jedan od legalnih operatora
	 * i vraca <code>true</code> ako je, <code>false</code> inace */
	private boolean isOperator(char ch) {
		if (ch == '-') {
			token = new Token("-", TokenType.OPERATOR);
			return true;
		} else if (ch == '+') {
			token = new Token("+", TokenType.OPERATOR);
			return true;
		}
		return false;
	}
}
