package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw06.shell.lexer.ShellTokenType;

/**
 * Razred koji modelira naredbu 'cd' u shell okruženju
 * @author Luka Dragutin
 *
 */
public class CdShellCommand implements ShellCommand{

	/** Naziv naredbe */
	private static final String COMMAND_NAME = "cd";
	
	/** Opis naredbe */
	private final List<String> commandDescription = Collections.unmodifiableList(Arrays.asList(new String[] {
			"Changes the current directory to the one given as argument. "
	}));
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException ako je zadan krivi broj ili oblik argumenata.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments == null) {
			throw new IllegalArgumentException("The command cd requires an argument!");
		}
		var lexer = new ShellLexer(arguments);
		var token = lexer.nextToken();
		if(token.getType().equals(ShellTokenType.EOF) || !lexer.nextToken().getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Wrong number of arguments!");
		}
		var path = Paths.get(token.getValue());
		var newPath = env.getCurrentDirectory().resolve(path);
		env.setCurrentDirectory(newPath.normalize());
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDesription() {
		return commandDescription;
	}

}
