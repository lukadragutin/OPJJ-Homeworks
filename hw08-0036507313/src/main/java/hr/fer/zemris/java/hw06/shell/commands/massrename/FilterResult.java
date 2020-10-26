package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Razred koji modelira datoteku koja je 
 * zadovoljila regularan izraz iz naredbe {@link MassRenameShellCommand}.
 * Pohranjuje podatke o grupiranim dijelovima reg izraza
 * @author Luka Dragutin
 *
 */
public class FilterResult {

	/**
	 * Put do datoteke
	 */
	private Path path;
	
	/**
	 * Grupe koje su zadovoljile izraz
	 */
	private String[] groups;


	public FilterResult(Path path, String[] groups) {
		super();
		this.path = Objects.requireNonNull(path);
		this.groups = Objects.requireNonNull(groups);
	}

	/**
	 * Vraca broj grupa
	 * @return
	 */
	public int numberOfGroups() {
		return groups.length;
	}

	
	/**
	 * Vraca grupu pod trazenim indeksom
	 * @throws IllegalArgumentException Ako je indeks van dosega
	 */
	public String group(int index) {
		if (index > groups.length || index < 0) {
			throw new IllegalArgumentException("Index out of bounds!");
		}
		return groups[index];
	}


	@Override
	public String toString() {
		return path.getFileName().toString();
	}

}
