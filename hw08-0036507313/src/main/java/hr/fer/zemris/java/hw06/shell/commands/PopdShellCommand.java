package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Razred koji modelira naredbu 'popd' u shell okruženju
 * 
 * @author Luka Dragutin
 *
 */
public class PopdShellCommand implements ShellCommand {

	/** Naziv naredbe */
	private static final String COMMAND_NAME = "popd";

	/** Opis naredbe */
	private final List<String> commandDescription = Collections.unmodifiableList(Arrays.asList(
			new String[] { "Removes the directory from the top of the stack and", "makes it the current directory." }));

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException ako je predan krivi broj ili oblik
	 *                                  argumenata
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null) {
			throw new IllegalArgumentException("Command popd takes no arguments!");
		}
		var data = env.getSharedData("cdstack");
		if (data == null) {
			throw new IllegalArgumentException("No directories available!");
		} else {
			Stack<Path> stack = (Stack<Path>) data;
			Path path;
			try {
				path = stack.pop();
			} catch (EmptyStackException e) {
				throw new IllegalArgumentException("No directories available!");
			}
			env.setCurrentDirectory(path);
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
