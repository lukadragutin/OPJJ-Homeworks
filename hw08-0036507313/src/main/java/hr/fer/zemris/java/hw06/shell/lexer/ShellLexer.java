package hr.fer.zemris.java.hw06.shell.lexer;

import java.util.Objects;

/**
 * Lexer za obradu argumenata u shell programu
 * 
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

	private LexerState state = LexerState.PATH;

	private boolean stringLiteral = false;

	public ShellLexer(String arguments) {
		data = Objects.requireNonNull(arguments).trim().toCharArray();
	}
	
	public void setState(LexerState state) {
		this.state = Objects.requireNonNull(state);
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
		if (currentIndex >= data.length) {
			token = new ShellToken<String>(null, ShellTokenType.EOF);
			return token;
		}
		char ch = data[currentIndex++];
		while (Character.isWhitespace(ch) && currentIndex < data.length) {
			ch = data[currentIndex++];
		}
		if (currentIndex >= data.length) {
			token = new ShellToken<String>(null, ShellTokenType.EOF);
			return token;
		}
		currentIndex--;
		if (state.equals(LexerState.PATH)) {
			if (ch == '"') {
				currentIndex++;
				generatePath2();
			} else {
				generatePath1();
			}
		} else {
			if (ch == '"') {
				stringLiteral = true;
			}
			if (stringLiteral) {
				currentIndex++;
				generateRename2();
			} else {
				generateRename1();
			}
		}
		return token;
	}

	private void generateRename2() {
		if (checkTags()) {
			return;
		} else {
			char ch = data[currentIndex++];
			StringBuilder sb = new StringBuilder();
			while (ch != '"') {
				if(isTag()) {
					currentIndex--;
					break;
				}
				sb.append(ch);
				if(currentIndex >= data.length) {
				  break;
				}
				ch = data[currentIndex++];
			}
			if(ch == '"') {
				stringLiteral = false;
			}
			token = new ShellToken<String>(sb.toString(), ShellTokenType.STRING);
		}

	}

	private boolean isTag() {
		char ch = data[currentIndex - 1];
		return ch == '}' || currentIndex < data.length && ch == '$' && data[currentIndex] == '{';
	}

	private boolean checkTags() {
		char ch = data[currentIndex];
		 if (ch == '}') {
			currentIndex++;
			token = new ShellToken<String>(null, ShellTokenType.ENDTAG);
			return true;
		} else if (currentIndex < data.length && ch == '$' && data[currentIndex + 1] == '{') {
			currentIndex += 2;
			token = new ShellToken<String>(null, ShellTokenType.OPENTAG);
			return true;
		}
		return false;
	}

	private void generateRename1() {
		if (checkTags()) {
			return;
		} else {
			char ch = data[currentIndex++];
			StringBuilder sb = new StringBuilder();
			while (!Character.isWhitespace(ch)) {
				if (isTag()) {
					currentIndex--;
					break;
				}

				sb.append(ch);
				if (currentIndex >= data.length) {
					break;
				}
				ch = data[currentIndex++];
			}
			token = new ShellToken<String>(sb.toString(), ShellTokenType.STRING);
		}
	}

	/**
	 * Pomoćna metoda za dohvat odredišta bez navodnika
	 */
	private void generatePath1() {
		var sb = new StringBuilder();
		char ch = data[currentIndex++];
		while (!Character.isWhitespace(ch)) {
			sb.append(ch);
			if (currentIndex == data.length) {
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
		if (currentIndex < data.length && !Character.isWhitespace(data[currentIndex])) {
			throw new IllegalArgumentException("Paths must be separated!");
		}
		
		token = new ShellToken<String>(sb.toString(), ShellTokenType.PATH);
	}
}
