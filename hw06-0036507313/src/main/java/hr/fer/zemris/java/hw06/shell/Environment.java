package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * Sučelje koje modelira okruženje za izvršavanje programa
 * koje komunicira s korisnikom preko komandne linije i prima
 * i izvršava naredbe.
 * @author Luka Dragutin
 *
 */
public interface Environment {

	/**
	 * Čita liniju iz definiranog ulaza.
	 * @return Pročitanu liniju teksta
	 * @throws ShellIOException Ako dođe do pogreške pri čitanju
	 */
	String readLine() throws ShellIOException;

	/**
	 * Piše predani tekst na definirani izlaz
	 * @param text Tekst za ispis
	 * @throws ShellIOException Ako dođe do pogreške pri čitanju
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Piše predani tekst na deifinirani izlaz u novu liniju
	 * @param text Tekst za ispis
	 * @throws ShellIOException Ako dođe do pogreške pri čitanju
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Dohvaća sve naredbe okruženja
	 * @return Read-only mapu svih naredbi pod ključem imena naredbi
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Dohvaća simbol koji se ispisuje pri upisivanju
	 * naredbi kroz više linija
	 */
	Character getMultilineSymbol();

	/**
	 * Postavlja simbol koji se ispisuje pri upisivanju
	 * naredbi kroz više linija
	 * @param symbol Simbol koji se postavlja
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Dohvaća simbol koji javlja korisniku dostupnost za upis naredbi
	 */
	Character getPromptSymbol();

	/**
	 * Postavlja simbol koji javlja korisniku dostupnost za upis naredbi
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Dohvaća simbol kojim korisnik signalizira protezanje
	 * naredbe kroz više linija
	 */
	Character getMorelinesSymbol();

	/**
	 * Postavlja simbol kojim korisnik signalizira protezanje
	 * naredbe kroz više linija
	 */
	void setMorelinesSymbol(Character symbol);
}
