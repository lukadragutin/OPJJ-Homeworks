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
 * Razred koji modelira naredbu 'dropd' u shell okruženju
 * @author Luka Dragutin
 *
 */
public class DropdShellCommand implements ShellCommand {

	/** Naziv naredbe */
	private static final String COMMAND_NAME = "dropd";
	
	/** Opis naredbe */
	private final List<String> commandDescription = Collections.unmodifiableList(Arrays.asList(new String[] {
			"Drops the top directory from the stack. The current directory stays the same."
	}));
	
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException ako je predan krivi broj ili oblik argumenata
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments != null) {
			throw new IllegalArgumentException("Command popd takes no arguments!");
		}
		var data = env.getSharedData("cdstack");
		if(data == null) {
			throw new IllegalArgumentException("No directories available!");
		}
		Stack<Path> stack = (Stack<Path>) data;
		if(stack.isEmpty()) {
			throw new IllegalArgumentException("No directories avaliable!");
		}
		stack.pop();
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
