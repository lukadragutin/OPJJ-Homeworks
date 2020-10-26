package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Razred koji modelira naredbu 'symbol' u Shell okruženju.
 * @author Luka Dragutin
 */
public class SymbolShellCommand implements ShellCommand {

	private final String commandName = "symbol";
	private final List<String> commandDescription = Collections
			.unmodifiableList(Arrays.asList(new String[] {
					"Enables the user to check or change the prompt, multiline and morelines symbols.",
					"i.e. symbol PROMPT - checks existing prompt symbol",
					"     symbol PROMPT # - changes the existing prompt symbol to '#'"
			}));

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako je zatražen nepostojeći simbol
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		var split = arguments.trim().split("\\s+");
		switch (split[0]) {
		case "PROMPT":
			symbolCommand(split, env, ShellSymbol.PROMPT);
			break;
		case "MULTILINE":
			symbolCommand(split, env, ShellSymbol.MULTILINE);
			break;
		case "MORELINES":
			symbolCommand(split, env, ShellSymbol.MORELINES);
			break;
		default:
			throw new IllegalArgumentException("Wrong symbol name!");
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Metoda koja ovisno o broju argumenata dohvaća ili
	 * postavlja simbol
	 * @param split Argumenti
	 * @param env Okruženje u kojem se program izvršava
	 * @param symb Tip simbola koji se mijenja/dohvaća
	 */
	private void symbolCommand(String[] split, Environment env, ShellSymbol symb) {
		if (split.length < 2) {
			env.writeln("Symbol for " + symb + " is '" + getSymbol(env, symb) + "'");
		} else if (split.length == 2) {
			if (split[1].length() == 1) {
				env.writeln("Symbol for " + symb + " changed from '" + getSymbol(env, symb) + "' to '" + split[1] + "'");
				setSymbol(env, symb, split[1].charAt(0));
			}
		}
	}

	/**
	 * Pomoćna metoda za dohvat traženog {@link ShellSymbol}
	 * @param env Okruženje u kojem se izvršava program
	 * @param symb Traženi tip simbola
	 * @return Traženi trenutni simbol
	 * @throws IllegalArgumentException Ako je zatražen nepostojeći tip simbola
	 */
	private Character getSymbol(Environment env, ShellSymbol symb) {
		switch (symb) {
		case MORELINES:
			return env.getMorelinesSymbol();
		case MULTILINE:
			return env.getMultilineSymbol();
		case PROMPT:
			return env.getPromptSymbol();
		default:
			throw new IllegalArgumentException("Wrong symbol!");
		}
	}

	/**
	 * Pomoćna metoda za promjenu traženog
	 * tipa simbola na predanu novu vrijednost
	 * @param env Okruženje u kojem se program izvršava
	 * @param symb Traženi tip simbola
	 * @param ch Novi simbol
	 * @throws IllegalArgumentException Ako je predan nepostojeći tip simbola
	 */
	private void setSymbol(Environment env, ShellSymbol symb, char ch) {
		switch (symb) {
		case MORELINES:
			env.setMorelinesSymbol(ch);
			break;
		case MULTILINE:
			env.setMultilineSymbol(ch);
			break;
		case PROMPT:
			env.setPromptSymbol(ch);
			break;
		default:
			throw new IllegalArgumentException("Non-existent symbol type!");
		}
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getCommandDesription() {
		return commandDescription;
	}

}
