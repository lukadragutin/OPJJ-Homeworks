package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Razred koji modelira naredbu 'charsets' u Shell okruženju.
 * @author Luka Dragutin
 *
 */
public class CharsetsShellCommand implements ShellCommand {

	private final static String COMMAND_NAME = "charsets";
	private final List<String> commandDescription = Collections
			.unmodifiableList(Arrays.asList(new String[] {
					"Writes out all of the possible charsets supported by this system.",
					"It does not accept any arguments."
			}));
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako su predani argumenti
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments != null) {
			throw new IllegalArgumentException("Command 'charsets' takes no arguments!");
		}
		var charsets = Charset.availableCharsets();
		env.writeln("All avaliable charsets: ");
		for(var c : charsets.keySet()) {
			env.writeln(c.toString());
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
