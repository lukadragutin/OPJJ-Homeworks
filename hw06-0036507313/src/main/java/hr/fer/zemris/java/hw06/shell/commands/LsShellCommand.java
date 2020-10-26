package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw06.shell.lexer.ShellTokenType;

/**
 * Razred koji modelira naredbu 'ls' u Shell okruženju.
 * @author Luka Dragutin
 *
 */
public class LsShellCommand implements ShellCommand {

	/**
	 * Pomoćni razred za modeliranje zapisa o datoteci iz
	 * naredbe ls
	 * @author Luka Dragutin
	 *
	 */
	private static class LsFormat implements Comparable<LsFormat> {
		/**
		 * Osnovni atributi datoteke(isDirectory, readable, writeable, executable) 
		 */
		private String basicAttributes;
		
		/**
		 * Velicina datoteke
		 */
		private String size;
		/**
		 * Vrijeme kreiranja datoteke
		 */
		private String creationTime;
		/**Ime datoteke*/
		private String name;

		/**
		 * Formatira kolekciju zapisa da su vrijednosti velicine formatirane udesno
		 * @param c Kolekcija LsFormata koja se formatira
		 */
		private static void format(Collection<LsFormat> c) {
			int len = 0;
			for (var l : c) {
				len = l.size.length() > len ? l.size.length() : len;
			}
			for (var l : c) {
				l.size = (" ").repeat(len - l.size.length()) + l.size;
			}
		}

		@Override
		public String toString() {
			return basicAttributes + "   " + size + " " + creationTime + " " + name;
		}

		@Override
		public int compareTo(LsFormat o) {
			return this.name.compareTo(o.name);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, size);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof LsFormat))
				return false;
			LsFormat other = (LsFormat) obj;
			return Objects.equals(name, other.name) && Objects.equals(size, other.size);
		}
		
		

	}

	private final static String COMMAND_NAME = "ls";
	private final List<String> commandDescription = Collections
			.unmodifiableList(Arrays.asList(new String[] {
					"The command 'ls' lists all the files in the directory",
					"given as argument and writes out their basic attributes.",
					"The output consists of 4 columns. First column indicates if current object",
					" is directory (d), readable (r), writable (w) and executable (x).",
					"Second column contains object size in bytes that is right aligned and occupies 10 characters.",
					"Follows file creation date/time and finally file name.",
			}));

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako je predano više/manje argumenata ili
	 * je put do direktorija nevažeći (neispravan)
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
		Path dir = Paths.get(token.getValue());
		var files = dir.toFile().listFiles();
		if (files == null) {
			throw new IllegalArgumentException("Given argument is not a directory!");
		}
		printAttributes(files, env);
		return ShellStatus.CONTINUE;
	}

	/**
	 * Pomoćna metoda koja formatira atribute datoteke za ispis
	 * @param files Polje datoteka koje se trebaju ispisati
	 * @param env Okruženje s kojim s komunicira metoda (ispis)
	 */
	private void printAttributes(File[] files, Environment env) {
		var list = new TreeSet<LsFormat>();
		for (File f : files) {
			var ls1 = new LsFormat();
			ls1.basicAttributes = getBasicAttributes(f);
			BasicFileAttributeView faView = Files.getFileAttributeView(f.toPath(), BasicFileAttributeView.class,
					LinkOption.NOFOLLOW_LINKS);
			BasicFileAttributes attributes;
			try {
				attributes = faView.readAttributes();
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			long fileSize = attributes.size();
			ls1.size = String.valueOf(fileSize);
			FileTime fileTime = attributes.creationTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
			ls1.creationTime = formattedDateTime;
			ls1.name = f.getName();
			list.add(ls1);
		}
		LsFormat.format(list);
		list.forEach(e -> env.writeln(e.toString()));
	}

	/**
	 * Pomoćna metoda za generiranja zapisa osnovnih atributa datoteke
	 * @param f Datoteka kojoj se provjeravaju atributi
	 * @return Zapis osnovnih atributa.
	 */
	private String getBasicAttributes(File f) {
		StringBuilder sb = new StringBuilder();
		sb.append(f.isDirectory() ? "d" : "-");
		sb.append(f.canRead() ? "r" : "-");
		sb.append(f.canWrite() ? "w" : "-");
		sb.append(f.canExecute() ? "x" : "-");
		return sb.toString();

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
