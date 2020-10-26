package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
 * Razred koji modelira naredbu 'cat' u Shell okruženj.
 * 
 * @author Luka Dragutin
 */
public class CatShellCommand implements ShellCommand {

	private final static String COMMAND_NAME = "cat";
	private final List<String> commandDescription = Collections.unmodifiableList(Arrays.asList(new String[] {
			"Writes the file from the path given as argument", "using the given charset for decoding. If no charset is",
			"given as argument, the system's default charset is used." }));

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException Ako su argumenti krivo zadani ili
	 *                                  nepostojeći
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null) {
			throw new IllegalArgumentException("Must specify file to open!");
		}
		var lexer = new ShellLexer(arguments);
		var token = lexer.nextToken();
		if (token.getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Must specify file to open!");
		}
		Path file = env.getCurrentDirectory().resolve(Paths.get(token.getValue())).normalize();
		if (!file.toFile().isFile()) {
			throw new IllegalArgumentException("File does not exist!");
		}
		token = lexer.nextToken();
		Charset cs;
		if (token.getType().equals(ShellTokenType.EOF)) {
			cs = Charset.defaultCharset();
		} else {
			if (Charset.isSupported(token.getValue())) {
				cs = Charset.forName(token.getValue());
			} else {
				throw new IllegalArgumentException("Unsupported charset!");
			}
		}
		if (!lexer.nextToken().getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Only two arguments needed!");
		}
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new BufferedInputStream(Files.newInputStream(file)), cs))) {
			String line = br.readLine();
			while (line != null) {
				env.writeln(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return null;
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
