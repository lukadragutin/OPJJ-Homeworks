package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Razred koji modelira naredbu 'help' u Shell okruženju.
 * @author Luka Dragutin
 *
 */
public class HelpShellCommand implements ShellCommand{
	
	private static final String COMMAND_NAME = "help";
	private final List<String> commandDescription = Collections
			.unmodifiableList(Arrays.asList(new String[] {
					"Lists all available commands if there are no arguments or",
					"writes out a short description of the wanted command."
			}));

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako je argument neispravan         
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments == null) {
			env.writeln("List of avaliable commands: ");
			env.commands().keySet().forEach((e) -> env.writeln(e.toString()));
			return ShellStatus.CONTINUE;
		}
		var command = env.commands().get(arguments.trim());
		if(command == null) {
			throw new IllegalArgumentException("The command '" + arguments + "' does not exist!");
		}
		else {
			env.writeln(command.getCommandName() + ": ");
			command.getCommandDesription().forEach(e -> env.writeln(e));
		}
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
