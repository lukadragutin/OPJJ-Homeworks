package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;


/**
 * Razred koji modelira naredbu 'pwd' u shell okruženju
 * @author Luka Dragutin
 *
 */
public class PwdShellCommand implements ShellCommand{
	
	
	/** Naziv naredbe */
	private static final String COMMAND_NAME = "pwd"; 
	
	/** Opis naredbe */
	private final List<String> commandDescription = Collections.unmodifiableList(Arrays.asList(new String[] {
			"Writes out the path to the current directory."
	}));
	

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako je predan krivi broj ili oblik argumenata
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments != null) {
			throw new IllegalArgumentException("Command pwd takes no arguments!");
		}
		env.writeln(env.getCurrentDirectory().toString());
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
