package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Modelira naredbe koje se izvršavaju u okruženju {@link Environment}
 * @author Luka Dragutin
 *
 */
public interface ShellCommand {

	/**
	 * Izvršavanje naredbe opisane u njezinoj statičkoj
	 * varijabli commandDescription dohvatljive metodom {@link getCommandDescription}
	 * @param env Okruženje u kojoj se izvršava naredba
	 * @param arguments Argumenti naredbe
	 * @return Poruka o nastavku ili prekidu programa
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Dohvaća ime naredbe
	 */
	String getCommandName();

	/**
	 * Dohvaća kratki opis naredbe
	 */
	List<String> getCommandDesription();
}
