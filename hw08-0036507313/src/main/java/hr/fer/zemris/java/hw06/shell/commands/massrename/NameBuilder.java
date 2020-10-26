package hr.fer.zemris.java.hw06.shell.commands.massrename;

/**
 * Sucelje koje modelira strategiju za 
 * 'proizvodnju'/parsiranje novog imena datoteke
 * iz naredbe {@link MassRenameShellCommand}
 * @author Luka Dragutin
 *
 */
public interface NameBuilder {

	/**
	 * Za datoteku koja je {@link FilterResult}, 
	 * nadograduje postojeci naziv ime
	 */
	void execute(FilterResult result, StringBuilder sb);
	
}
