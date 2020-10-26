package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw06.shell.lexer.ShellTokenType;

/**
 * Razred koji modelira naredbu 'mkdir' u Shell okruženju.
 * @author Luka Dragutin
 *
 */
public class MkdirShellCommand implements ShellCommand{
	
	private final static String COMMAND_NAME = "mkdir";
	private final List<String> commandDescription = Collections
			.unmodifiableList(Arrays.asList(new String[] {
					"This command creates a directory from the path given as the command's argument.",
					"It also creates any non existant parent directories to the requested directory."}));

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako su argumenti neispravni
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments == null) {
			throw new IllegalArgumentException("Command 'mkdir' requires a directory name!");
		}
		var lexer = new ShellLexer(arguments);
		var token = lexer.nextToken();
		if(token.getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("No directory given!");
		}
		if(!lexer.nextToken().getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Only one path needed!");
		}
		Path dir = env.getCurrentDirectory().resolve(token.getValue()).normalize();
		File file = dir.toFile();
		if(file.exists()) {
			throw new IllegalArgumentException("The directory already exists!");
		}
		file.mkdirs();
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
