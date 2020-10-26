package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
 * Razred koji modelira naredbu 'copy' u Shell okruženju.
 * @author Luka Dragutin
 *
 */
public class CopyShellCommand implements ShellCommand {

	private final static String COMMAND_NAME = "copy";
	private final List<String> commandDescription = Collections
			.unmodifiableList(Arrays.asList(new String[] { "The copy command copies the file given as argument",
					"to the directory specified. Target file name can be given",
					"but if left out the name is set to the original file name.",
					"If a file with the target file name already exists user will be", "prompted to overwrite it." }));

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako su argumenti krivo zadani.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null) {
			throw new IllegalArgumentException("Must specify the file to copy and its target!");
		}
		var lexer = new ShellLexer(arguments);
		var token = lexer.nextToken();
		if (token.getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Empty path!");
		}
		var path1 = env.getCurrentDirectory().resolve(token.getValue()).normalize();
		File file = path1.toFile();
		if (!file.isFile()) {
			throw new IllegalArgumentException("First argument has to be a path to a file!");
		}
		token = lexer.nextToken();
		if (token.getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Path to target directory has to be given!");
		}
		var path2 = env.getCurrentDirectory().resolve(token.getValue()).normalize();
		File target = path2.toFile();
		if (target.isFile() && target.exists()) {
			if (!askForOverwrite(env)) {
				return ShellStatus.CONTINUE;
			}
		} else if (target.isDirectory() && !target.exists()) {
			throw new IllegalArgumentException("Target directoy does not exist!");
		} else if (target.isDirectory() && target.exists()) {
			String targetPath = target.toPath().toString();
			String fileName = file.getName();
			target = Paths.get(targetPath + "/" + fileName).toFile();
			if (target.isFile() && target.exists()) {
				if (!askForOverwrite(env)) {
					return ShellStatus.CONTINUE;
				}
			}
		}
		if (!lexer.nextToken().getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Only two path arguments required!");
		}
		copyFile(file, target);
		return ShellStatus.CONTINUE;
	}

	/**
	 * Pomoćna metoda za kopiranje datoteke na predanu lokaciju.
	 * @param file Originalna kopirana datoteka
	 * @param target Odredište kopije datoteke
	 * @throws IllegalArgumentException Ako su predana datoteka i/ili
	 * odredište neispravni
	 */
	private void copyFile(File file, File target) {
		byte[] buffer = new byte[4096];
		try (BufferedInputStream bs = new BufferedInputStream(Files.newInputStream(file.toPath()));
				BufferedOutputStream os = new BufferedOutputStream(Files.newOutputStream(target.toPath()))) {
			int status = bs.read(buffer);
			while (status != -1) {
				os.write(buffer, 0, status);
				status = bs.read(buffer);
			}
			os.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

	}

	/**
	 * Pomoćna metoda koja pita korisnika od prebrisavanju postojeće datoteke.
	 * @param env Okruženje u kojem se program izvršava
	 * @return <code>true</code> ako korisnik želi prebrisati, <code>false</code> ako ne želi.
	 */
	private boolean askForOverwrite(Environment env) {
		env.write("The target file already exists. Do you want to overwrite it? (y/n): ");
		while (true) {
			String reply = env.readLine();
			if (reply.equals("y")) {
				return true;
			} else if (reply.equals("n")) {
				return false;
			} else {
				env.write("'" + reply + "' is not a valid reply. Try again... (y/n): ");
			}
		}

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
