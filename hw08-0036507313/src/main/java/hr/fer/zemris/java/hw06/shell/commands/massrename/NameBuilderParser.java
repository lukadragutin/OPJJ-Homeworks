package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.lexer.LexerState;
import hr.fer.zemris.java.hw06.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw06.shell.lexer.ShellToken;
import hr.fer.zemris.java.hw06.shell.lexer.ShellTokenType;

/**
 * Na osnovu izraza iz naredbe {@link MassRenameShellCommand}
 * mijenja ime datotekama. Koristi se primjercima {@link NameBuilder}
 * @author Luka Dragutin
 *
 */
public class NameBuilderParser {

	private NameBuilder nb;
	private ArrayList<NameBuilder> list;
	private ShellLexer lexer;

	public NameBuilderParser(String izraz) {
		lexer = new ShellLexer(izraz);
		lexer.setState(LexerState.RENAME);
		list = new ArrayList<>();
		parse();
		nb = new NameBuilderImpl(list);
	}

	private void parse() {
		var token = lexer.nextToken();
		while (!token.getType().equals(ShellTokenType.EOF)) {
			if (token.getType().equals(ShellTokenType.STRING)) {
				parseString(token);
			} else {
				parseTag();
			}
			token = lexer.nextToken();
		}
	}

	/**
	 * Pomocna metoda za parsiranje dijela izraza unutar zagrade '${}'
	 */
	private void parseTag() {
		int group;
		int space = 0;
		boolean zero = false;
		
		var token = lexer.nextToken();
		String value = token.getValue();
		
		if (!value.contains(",")) {
			try {
				group = Integer.parseInt(value.trim());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Not a valid pattern!");
			}
		} else {
			var split = value.split(",");
			try {
				group = Integer.parseInt(split[0].trim());
				if (split[1].startsWith("0")) {
					space = Integer.parseInt(split[1].substring(1));
					zero = true;
				} else {
					space = Integer.parseInt(split[1]);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Not a valid pattern!");
			}
		}
		
		if(space == 0) {
			list.add(DefaultNameBuilders.group(group));
		}
		else {
			list.add(DefaultNameBuilders.group(group, zero ? '0' : ' ', space));
		}
		
		if(!lexer.nextToken().getType().equals(ShellTokenType.ENDTAG)) {
			throw new IllegalArgumentException("Not a valid pattern!");
		}
	}

	
	/**
	 * 
	 * Parsira dio izraza koji je obicni string
	 */
	private void parseString(ShellToken<String> token) {
		list.add(DefaultNameBuilders.text(token.getValue()));
	}

	/**
	 * Vraca {@link NameBuilder}
	 */
	public NameBuilder getNameBuilder() {
		return nb;
	}
	
	/**
	 * Privatna klasa koja implementira {@link NameBuilder} koji
	 * pohranjuje reference na ostale primjerke
	 */
	private static class NameBuilderImpl implements NameBuilder {

		/**
		 * Lista referenci na {@link NameBuilder}
		 */
		private List<NameBuilder> list;
		
		
		public NameBuilderImpl(List<NameBuilder> list) {
			this.list = list;
		}

		@Override
		public void execute(FilterResult result, StringBuilder sb) {
			list.forEach((e) -> e.execute(result, sb));
		}
	}
	
	
}
