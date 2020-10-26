package hr.fer.zemris.java.hw06.shell.commands.massrename;

/**
 * Razred koji nudi implementacije sucelja {@link NameBuilder}
 * @author Luka Dragutin
 *
 */
public class DefaultNameBuilders {

	/**
	 * U ime dodaje samo predodredeni niz znakova
	 */
	public static NameBuilder text(String t) {
		return (r, s) -> s.append(t);
	}
	
	/**
	 * U ime dodaje ekvivalent grupe pod predanim
	 * indeksom dobivene iz regularnog izraza
	 * @param index Trazena grupa
	 */
	public static NameBuilder group(int index) {
		return (f,sb) -> {
			sb.append(f.group(index));
		};
	}
	
	/**
	 * Kao {@link DefaultNameBuilders#group(int)}, ali jos
	 * i propisuje minimalnu duljinu koju ce zauzimati izraz iz grupe.
	 * Duljina se ostvaruje popunjavanjem sa znakom {@code padding} 
	 * @param index Indeks trazene grupe
	 * @param padding Znak kojim se popuni niz do minimalne duljine
	 * @param minWidth Minimalna duljina niza iz grupe 
	 */
	public static NameBuilder group(int index, char padding, int minWidth) {
		return (f,sb) -> {
			var gr = f.group(index);
			sb.append(String.valueOf(padding).repeat(gr.length() > minWidth ? 0 : minWidth - gr.length()) + gr);
		};
	}
	
}
