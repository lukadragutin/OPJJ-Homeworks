package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Razred koji modificira naredbu 'exit' u Shell okruženju
 * @author Luka Dragutin
 *
 */
public class ExitShellCommand implements ShellCommand {

	private final static String COMMAND_NAME = "exit";
	private final List<String> commandDescription = Collections.unmodifiableList(Arrays
			.asList(new String[] { "The exit command exits the shell and turns the application off." }));

	/**
	 * {@inheritDoc}
	 *  @throws IllegalArgumentException Ako su predani argumenti
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments != null) {
			throw new IllegalArgumentException("Command 'exit' takes no arguments!");
		}
		return ShellStatus.TERMINATE;
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
