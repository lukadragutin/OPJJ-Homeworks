package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;


/**
 * Razred koji modelira naredbu 'listd' u shell okruženju
 * @author Luka Dragutin
 *
 */
public class ListdShellCommand implements ShellCommand {

	/** naziv naredbe */
	private static final String COMMAND_NAME = "listd";
	
	/** Opis naredbe */
	private final List<String> commandDescription = Collections.unmodifiableList(Arrays.asList(new String[] {
			"Lists the contents of the stack keeping the directories.",
			"The current directory stays the same."
	}));
			
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException ako je predan krivi broj ili oblik argumenata
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments != null) {
			throw new IllegalArgumentException("The command listd takes no arguments!");
		}
		var data = env.getSharedData("cdstack");
		if(data == null) {
			throw new IllegalArgumentException("No stored directories!");
		}
		var stack = (Stack<Path>) data;
		if(stack.isEmpty()) {
			throw new IllegalArgumentException("No stored directories!");
		}
		else {
			stack.forEach((e) -> env.writeln(e.toString()));
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
