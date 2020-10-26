package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Sucelje koje ocrtava matematicku operaciju
 * @author Luka Dragutin
 *
 * @param <T> Tip varijabli
 */
public interface Operation<T extends Number> {
	
	/**
	 * Izvsava zadanu matematicku operaciju nad predanim objektima
	 * @param x Prvi argument
	 * @param y Drugi argument
	 * @return Rezultat operacije
	 */
	T execute(T x, T y);
	
}
