package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Lexer koji koristi razred {@link SmartScriptParser} za parsiranje Stringova.
 * Vraca tokene na zahtjev pozivatelja. Moze raditi u TEXT ili TAG nacinu.
 * 
 * @author Luka
 *
 */
public class ScriptingLexer {

	/**
	 * Polje znakova nastalo od ulaznog Stringa
	 */
	private ScriptToken token;

	/**
	 * Posljednji generirani token
	 */
	private char[] data;

	/**
	 * Indeks trenutnog znaka u polju data
	 */
	private int currentIndex;

	/**
	 * Trenutno stanje rada lexera, TEXT ili TAG
	 */
	private LexerState state;

	/**
	 * Stvara novi lexer sa ulaznim Stringom text koji rastavlja na polje charova
	 * data, te po defaultu postalja nacin rada u BASIC
	 * 
	 * @param text Ulazni String koji se obraduje
	 * @throws NullPointerException ako je text null
	 * @param body
	 */
	public ScriptingLexer(String body) {
		data = Objects.requireNonNull(body.trim()).toCharArray();
		state = LexerState.TEXT;
	}

	/**
	 * Vraca posljednji generiran token
	 * 
	 * @return posljednji token, clanska varijabla token
	 */
	public ScriptToken getToken() {
		return token;
	}

	/**
	 * Zahtjeva od lexera da generira iduci token
	 * 
	 * @return Novi token
	 */
	public ScriptToken nextToken() {
		if (state == LexerState.TEXT) {
			generateTokenText();
		} else {
			generateTokenTag();
		}
		return token;
	}

	/**
	 * Vraca trenutno stanje rada lexera
	 * 
	 * @return {@link LexerState} clansku varijablu state
	 */
	public LexerState getState() {
		return state;
	}

	/**
	 * pomocna metoda za parsiranje teksta izmedu tagova
	 * 
	 * @throws LexerException ako je uneseni tekst krivi
	 */
	private void generateTokenText() {
		if (currentIndex > data.length) {
			throw new LexerException("No more entries");
		}
		if (currentIndex == data.length) {
			token = new ScriptToken(ScriptTokenType.EOF, null);
			return;
		}
		char ch = data[currentIndex++];
		if (ch == '{' && currentIndex < data.length && data[currentIndex] == '$') {
			token = new ScriptToken(ScriptTokenType.STARTTAG, null);
			state = LexerState.TAG;
			currentIndex++;
			return;
		}
		String word = "";
		do {
			if (currentIndex == data.length) {
				word += ch;
				currentIndex++;
				break;
			}
			if (ch == '\\') {
				if (currentIndex == data.length) {
					break;
				}
				ch = data[currentIndex++];
				if (ch != '\\' && ch != '{') {
					throw new LexerException("Wrong escape sequence!");
				} else if (ch == '}' && data[currentIndex] == '$') {
					word += ch;
					ch = data[currentIndex++];
				}
			}
			word += ch;
			ch = data[currentIndex++];
		} while (ch != '{');
		currentIndex--;
		token = new ScriptToken(ScriptTokenType.STRING, word);
	}

	/**
	 * Pomocna metoda za generiranje tokena sa argumentima iz tag izraza
	 * 
	 * @throws LexerException ako je krivo napisan tag izraz
	 */
	private void generateTokenTag() {
		if (currentIndex > data.length) {
			throw new LexerException("No more entries");
		}

		char ch;
		do {
			if (currentIndex == data.length) {
				token = new ScriptToken(ScriptTokenType.EOF, null);
				return;
			}
			ch = data[currentIndex++];
		} while (Character.isWhitespace(ch));
		currentIndex--;
		if (Character.isLetter(ch)) {
			generateVariable();
		} else if (Character.isDigit(ch)) {
			generateNumber();
		} else if (ch == '"') {
			generateString();
		} else {
			generateOperator();
		}

	}

	/**
	 * Generira token sa operatorom u tag izrazu
	 * 
	 * @throws LexerException ako je neispravan operator
	 */
	private void generateOperator() {
		char ch = data[currentIndex++];
		if (currentIndex == data.length) {
			throw new LexerException("Wrong tag input");
		}
		if (ch == '$' && data[currentIndex] == '}') {
			state = LexerState.TEXT;
			currentIndex++;
			token = new ScriptToken(ScriptTokenType.ENDTAG, null);
			return;
		}
		if (!isValid(ch)) {
			throw new LexerException("Wrong operator input");
		}
		if (ch == '@') {
			generateVariable();
			ScriptToken variable = getToken();
			String function = String.valueOf(ch);
			function += variable.getValue().toString();
			token = new ScriptToken(ScriptTokenType.FUNCTION, function);
			return;
		}
		if (ch == '-' && Character.isDigit(data[currentIndex])) {
			currentIndex--;
			generateNumber();
		}
		if (ch == '=') {
			token = new ScriptToken(ScriptTokenType.VARIABLE, ch);
		}
		token = new ScriptToken(ScriptTokenType.OPERATOR, ch);
	}

	/**
	 * Pomocna metoda koja provjerava je li znak na popisu ispravnih znakova
	 * 
	 * @param ch znak koji se provjerava
	 * @return true ako je znak ispravan, false ako nije
	 */
	private boolean isValid(char ch) {
		if (ch != '*' && ch != '/' && ch != '+' && ch != '-' && ch != '^' && ch != '=' && ch != '@') {
			return false;
		}
		return true;
	}

	/**
	 * Generira String kao argument u tag izrazu
	 * 
	 * @throws LexerException ako je krivo napisan string
	 */
	private void generateString() {
		String word = "";
		char ch = data[currentIndex++];
		do {
			if (ch == '\\') {
				processEscapeTag();
				ch = data[currentIndex++];
			}
			word += ch;
			if (currentIndex == data.length) {
				throw new LexerException("Wrong tag input");
			}
			ch = data[currentIndex++];
		} while (ch != '"');
		word += ch;
		token = new ScriptToken(ScriptTokenType.STRING, word.substring(1, word.length() - 1));
	}

	/**
	 * Obraduje pojavu escape znaka
	 */
	private void processEscapeTag() {
		if (currentIndex == data.length) {
			throw new LexerException("Wrong tag input");
		}
		char ch = data[currentIndex];
		if(ch == 'r' || ch == 'n') {
			currentIndex--;
		}
		else if (ch != '\\' && ch != '"') {
			throw new LexerException("Wrong escape sequence");
		}
	}

	/**
	 * Generira token sa brojcanom vrijednosti double ili int
	 * 
	 * @throws LexerException ako je prevelik broj
	 */
	private void generateNumber() {
		char ch = data[currentIndex++];
		String number = "";
		boolean dot = false;
		do {
			if (ch == '.') {
				dot = true;
			}
			number += ch;
			if (currentIndex == data.length) {
				currentIndex++;
				break;
			}
			ch = data[currentIndex++];
		} while (Character.isDigit(ch) || ((ch == '.') && !dot));
		currentIndex--;
		try {
			if (number.lastIndexOf('.') > -1) {
				token = new ScriptToken(ScriptTokenType.DOUBLE, Double.parseDouble(number));
			} else {
				token = new ScriptToken(ScriptTokenType.INTEGER, Integer.parseInt(number));
			}
		} catch (NumberFormatException ex) {
			throw new LexerException("Number out of range");
		}
	}

	/**
	 * Generira token sa varijablama iz tag izraza
	 */
	private void generateVariable() {
		char ch = data[currentIndex++];
		String word = "";
		do {
			word += ch;
			if (currentIndex == data.length) {
				currentIndex++;
				break;
			}
			ch = data[currentIndex++];
		} while (Character.isLetterOrDigit(ch) || ch == '_');
		currentIndex--;
		token = new ScriptToken(ScriptTokenType.VARIABLE, word);
	}
}
