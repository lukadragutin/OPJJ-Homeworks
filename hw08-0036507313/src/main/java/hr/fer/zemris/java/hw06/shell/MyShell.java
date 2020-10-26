package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.DropdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ListdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.massrename.MassRenameShellCommand;

/**
 * Okruženje koje omogućavak korisniku unos naredbi kojima
 * može upravljati datotekama. Podržava čitanje, kopiranje, pregled atributa datoteka,
 * stvaranje direktorija, ispis postojećih datoteka itd. Popis svih naredbi dohvaća
 * se naredbom help, a upisom help 'ime naredbe' se dobiva njezin kratki opis.
 * Izlazi se upisom naredbe 'exit'.
 * @author Luka Dragutin
 * @version v1.0
 */
public class MyShell {

	public static void main(String[] args) {
		Shell shell = new Shell();
		String commandName, arguments;
		ShellCommand command;
		ShellStatus status = ShellStatus.CONTINUE;
		shell.writeln("Welcome to MyShell v 1.0");
		do {
			shell.write(shell.getPromptSymbol().toString() + " ");
			String line = shell.readLine().trim();
			while (line.endsWith(shell.getMorelinesSymbol().toString())) {
				shell.write(shell.getMultilineSymbol().toString() + " ");
				line = line.substring(0, line.length() - 1) + shell.readLine().stripTrailing();
			}
			try {
				commandName = getCommandName(line, shell);
				arguments = getArgument(line);
				command = shell.commands().get(commandName);
				status = command.executeCommand(shell, arguments);
			} catch (IllegalArgumentException ex) {
				shell.writeln(ex.getMessage());
				continue;
			}
		} while (status != ShellStatus.TERMINATE);
		shell.closeScanner();
	}

	/**
	 * Iz unosa naredbe čita argumente
	 * @param line Unesena naredba
	 * @return Predani argumenti
	 */
	private static String getArgument(String line) {
		String[] arg = line.trim().split("\\s+", 2);
		if(arg.length < 2) return null;
		return arg[1];
	}

	/**
	 * Iz unosa dohvaća ime naredbe
	 * @param line Unos
	 * @param env Okruženje u kojem se program izvšava
	 * @return Tekstualni zapis naredbe
	 * @throws IllegalArgumentException Ako naredba nije na popisu
	 * podržanih naredbi
	 */
	private static String getCommandName(String line, Environment env) {
		int space = line.indexOf(' ');
		String commandName = line.trim().substring(0, space == -1 ? line.length() : space);
		if (!env.commands().containsKey(commandName)) {
			throw new IllegalArgumentException("Wrong command issued!");
		}
		return commandName;
	}

	/**
	 * Implementacija okruženja {@link Environment}
	 * @author Luka Dragutin
	 *
	 */
	public static class Shell implements Environment {

		/**
		 * Mapa podržanih naredbi
		 */
		private static SortedMap<String, ShellCommand> commands;
		
		/**
		 * Čitač unosa
		 */
		private Scanner sc;
		
		/**
		 * Simbol koji se ispisuje pri upisivanju
	     * naredbi kroz više linija
		 */
		private Character multiline = '|';
		
		/**
		 * Simbol koji javlja korisniku dostupnost za upis naredbi
		 */
		private Character prompt = '>';
		
		/**
		 * Simbol kojim korisnik signalizira protezanje
	     * naredbe kroz više linija
		 */
		private Character morelines = '\\';
		
		/**
		 * Trenutni direktorij
		 */
		private Path currentDirectory;

		/**
		 * Dijeljeni podatci medu naredbama
		 */
		private Map<String, Object> sharedData;

		static {
			commands = new TreeMap<>();
			commands.put("exit", new ExitShellCommand());
			commands.put("tree", new TreeShellCommand());
			commands.put("symbol", new SymbolShellCommand());
			commands.put("cat", new CatShellCommand());
			commands.put("ls", new LsShellCommand());
			commands.put("hexdump", new HexdumpShellCommand());
			commands.put("copy", new CopyShellCommand());
			commands.put("mkdir", new MkdirShellCommand());
			commands.put("help", new HelpShellCommand());
			commands.put("charsets", new CharsetsShellCommand());
			commands.put("pwd", new PwdShellCommand());
			commands.put("cd", new CdShellCommand());
			commands.put("pushd", new PushdShellCommand());
			commands.put("popd", new PopdShellCommand());
			commands.put("dropd", new DropdShellCommand());
			commands.put("massrename", new MassRenameShellCommand());
			commands.put("listd", new ListdShellCommand());
		}
		
		/**
		 * Inicijalizira sve naredbe i čitač unosa
		 */
		public Shell() {
			sc = new Scanner(System.in);
			currentDirectory = Paths.get(".").toAbsolutePath().normalize();
			sharedData = new HashMap<String, Object>();
		}

		@Override
		public String readLine() throws ShellIOException {
			try {
				return sc.nextLine();
			} catch (Exception ex) {
				throw new ShellIOException(ex.getMessage());
			}
		}

		@Override
		public void write(String text) throws ShellIOException {
			try {
				System.out.print(Objects.requireNonNull(text));
			} catch (Exception ex) {
				throw new ShellIOException(ex.getMessage());
			}
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			try {
				System.out.println(Objects.requireNonNull(text));
			} catch (Exception ex) {
				throw new ShellIOException(ex.getMessage());
			}
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return Collections.unmodifiableSortedMap(commands);
		}

		@Override
		public Character getMultilineSymbol() {
			return multiline;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			multiline = Objects.requireNonNull(symbol);
		}

		@Override
		public Character getPromptSymbol() {
			return prompt;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			prompt = Objects.requireNonNull(symbol);
		}

		@Override
		public Character getMorelinesSymbol() {
			return morelines;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			morelines = Objects.requireNonNull(symbol);
		}

		/**
		 * Zatvara čitač
		 */
		private void closeScanner() {
			sc.close();
		}

		@Override
		public Path getCurrentDirectory() {
			return currentDirectory;
		}

		@Override
		public void setCurrentDirectory(Path path) {
			if(!Files.exists(path) || !Files.isDirectory(path)) {
				throw new IllegalArgumentException("Path must be a valid directory!");
			}
			currentDirectory = path;
		}

		@Override
		public Object getSharedData(String key) {
			return sharedData.get(Objects.requireNonNull(key));
		}

		@Override
		public void setSharedData(String key, Object value) {
			sharedData.put(Objects.requireNonNull(key), value);
		}

	}
}
