package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw06.shell.lexer.ShellTokenType;


/**
 * Razred koji modelira naredbu 'pushd' u shell okruženju
 * @author Luka Dragutin
 *
 */
public class PushdShellCommand implements ShellCommand{

	/** Naziv naredbe */
	private static final String COMMAND_NAME = "pushd";
	
	/** Opis naredbe */
	private final List<String> commandDescription = Collections.unmodifiableList(Arrays.asList(new String[] {
			"Takes the given directory and makes it the current one.",
			"It pushes the old directory onto the stack."
	}));
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako je predan krivi broj ili oblik argumenata
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments == null) {
			throw new IllegalArgumentException("Command pushd can't have no arguments!");
		}
		var lexer = new ShellLexer(arguments);
		var token = lexer.nextToken();
		if (token.getType().equals(ShellTokenType.EOF) || !lexer.nextToken().getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Invalid number of arguments!");
		}
		Path path = env.getCurrentDirectory().resolve(Paths.get(token.getValue())).normalize();
		if(!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Not a valid path!");
		}
		
		Stack<Path> stack = (Stack<Path>) env.getSharedData("cdstack");
		if(stack == null) {
			stack = new Stack<Path>();
		}
		
		stack.push(env.getCurrentDirectory());
		env.setSharedData("cdstack", stack);
		env.setCurrentDirectory(path);
		
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
