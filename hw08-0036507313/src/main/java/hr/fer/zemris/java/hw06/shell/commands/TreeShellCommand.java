package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw06.shell.lexer.ShellTokenType;


/**
 * Razred koji modelira naredbu 'tree' u Shell okruženju.
 * @author Luka Dragutin
 *
 */
public class TreeShellCommand implements ShellCommand {

	private static final String COMMAND_NAME = "tree";
	private final List<String> commandDescription = Collections
			.unmodifiableList(Arrays.asList(new String[] {
					"Lists all files and directories that are direct or indirect children",
					"of the directory given as the command's argument. The argument has to",
					"be an already existing directory."
			}));

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako je put do datoteke nevažeći
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		if (arguments == null) {
			throw new IllegalArgumentException("A directory must be given!");
		}
		var lexer = new ShellLexer(arguments);
		var token = lexer.nextToken();
		if (token.getType().equals(ShellTokenType.EOF) || !lexer.nextToken().getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Invalid number of arguments!");
		}
		Path path = env.getCurrentDirectory().resolve(Paths.get(token.getValue())).normalize();
		try {
			Files.walkFileTree(path, new TreeWriter(env));
		} catch (IOException e) {
			env.writeln("Wrong path!");
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

	/**
	 * Pomoćni razred koji služi za obilazak svih direktorija i datoteka te njihov ispis 
	 * sa indentacijom ovisno o razini poddirektorija u kojem se datoteka /direktorij nalazi
	 * @author Luka Dragutin
	 */
	private static class TreeWriter implements FileVisitor<Path> {

		private int razina;
		private Environment env;

		public TreeWriter(Environment env) {
			this.env = Objects.requireNonNull(env);
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			env.writeln(" ".repeat(razina * 2)
					+ (razina == 0 ? dir.toAbsolutePath() : "\\" + dir.getName(dir.getNameCount() - 1)));
			razina++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			env.writeln(" ".repeat(razina * 2) + file.getName(file.getNameCount() - 1));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			razina--;
			return FileVisitResult.CONTINUE;
		}

	}
}
