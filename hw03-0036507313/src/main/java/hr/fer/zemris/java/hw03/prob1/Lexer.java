package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * Razred koji parsira ulazni niz znakova u tokene
 * na zahtjev pozivatelja. Moze raditi u dva nacina rada,
 * BASIC kad razlikuje rijeci, brojeve i znakove, te EXTENDED kad
 * spaja sve u jedan veliki String
 * @author Luka Dragutin
 *
 */
public class Lexer {

	/**
	 * Polje znakova nastalo od ulaznog Stringa
	 */
	private char[] data;
	
	/**
	 * Posljednji generirani token
	 */
	private Token token;
	
	/**
	 * Indeks trenutnog znaka u polju data
	 */
	private int currentIndex;
	
	/**
	 * Trenutno stanje lexera, BASIC ili EXTENDED
	 */
	private LexerState state;
	
	/**
	 * Stvara novi lexer sa ulaznim Stringom text koji rastavlja na
	 * polje charova data, te po defaultu postalja nacin rada u BASIC
	 * @param text Ulazni String koji se obraduje
	 * @throws NullPointerException ako je text null
	 */
	public Lexer(String text) {
		Objects.requireNonNull(text);
		data = text.toCharArray();
		state = LexerState.BASIC;
	}
	
	/**
	 * Zahtjeva od lexera da generira iduci token
	 * @return Novi token
	 */
	public Token nextToken() {
		generateToken();
		return token;
	}
	
	/**
	 * Vraca posljednji generiran token
	 * @return posljednji token, clanska varijabla token
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Postavlja stanje rada lexera na zeljeno stanje state
	 * @param state Stanje rada u koji se lexer prebacuje
	 */
	public void setState(LexerState state) {
		this.state = Objects.requireNonNull(state);
	}
	
	/**
	 * Pomocna metoda za generiranje iduceg tokena
	 * @throws LexerException ako nema vise znakova za obraditi
	 */
	private void generateToken() {
		if(currentIndex > data.length) {
			throw new LexerException("Nema vise znakova!");
		}
		char current;
		do {
			if(currentIndex == data.length) {
				generateEOF();
				return;
			}
			current = data[currentIndex++];
		} while(Character.isWhitespace(current));
		currentIndex--;
		if(state == LexerState.EXTENDED && current != '#') {
			generateWordExtended();
		} 
		else if(Character.isLetter(current) || current == '\\') {
			generateWord();
		}
		else if(Character.isDigit(current)) {
			generateNumber();
		}
		else {
			generateSymbol();
		}
	}
	/**
	 * pomocna metoda za generiranje iduceg tokena tipa word
	 */
	private void generateWord() {
		String word = "";
		char ch = data[currentIndex++];
		do  {
			if(ch == '\\') {
				ch = processEscape();
			}
			word += ch;
			if(currentIndex == data.length) {
				currentIndex++;
				break;
			}
			ch = data[currentIndex++];
		} while((Character.isLetter(ch) || ch == '\\' ));
		currentIndex--;
		token = new Token(TokenType.WORD, word);
	}
	
	/**
	 * Pomocna metoda za generiranje iduceg tokena u extended nacinu rada
	 */
	private void generateWordExtended() {
		String word = "";
		char ch = data[currentIndex++];
		do {
			word += ch;
			if(currentIndex == data.length) {
				currentIndex++;
				break;
			}
			ch = data[currentIndex++];
		} while(!Character.isWhitespace(ch) && ch != '#');
		currentIndex--;
		token = new Token(TokenType.WORD, word);
	}
	
	/**
	 * pomocna metoda za generiranje iduceg tokena tipa NUMBER
	 */
	private void generateNumber() {
		char ch = data[currentIndex++];
		String number = "";
		do {
			number+=ch;
			if(currentIndex == data.length) {
				currentIndex++;
				break;
			}
			ch = data[currentIndex++];
		} while(Character.isDigit(ch));
		currentIndex--;
		try {
			token = new Token(TokenType.NUMBER, Long.parseLong(number));
		} catch (NumberFormatException ex) {
			throw new LexerException("Number out of range");
		}
	}
	/**
	 * Generira token tipa EOF
	 */
	private void generateEOF() {
		token = new Token(TokenType.EOF, null);
		currentIndex++;
	}
	
	/**
	 * Generira token tipa SYMBOL
	 */
	private void generateSymbol() {
		token = new Token(TokenType.SYMBOL, data[currentIndex]);
		if(data[currentIndex] == '#') {
			setState(state == LexerState.BASIC ? LexerState.EXTENDED : LexerState.BASIC);
		}
		currentIndex++;
	}
	
	/**
	 * Pomocna metoda za obradu izraza sa escapeom (\)
	 * @return char koji slijedi nakon znaka escape
	 * @throws LexerException ako je krivi znak nakon escapea
	 */
	private char processEscape() {
		char ch;
		if(currentIndex < data.length) {
			ch = data[currentIndex++];
			if(Character.isLetter(ch)) {
				throw new LexerException();
			}
		}
		else {
			throw new LexerException();
		}
		return ch;
	}
}

