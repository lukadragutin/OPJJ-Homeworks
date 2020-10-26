package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Sucelje koje modelira metode iteratora nad kolekcijama iz 
 * paketa hr.fer.zemris.java.custom.collections
 * @author Luka Dragutin
 *
 */
public interface ElementsGetter {
	
	/**
	 * Metoda provjerava ima li jos elemenata
	 * u kolekciji
	 * @return boolean true ako ima jos elemenata, false ako nema
	 */
	public boolean hasNextElement();
	
	/**
	 * Metoda dohvaca iduci element kolekcije i vraca ga.
	 * @return Object koji je iduci element iz kolekcije
	 */
	public Object getNextElement();
	
	/**
	 * Nad svim preostalim elementima kolekcije poziva 
	 * metodu procesora p process(Object value).
	 * @param p Procesor koji hocemo pozvati nad
	 *  preostalim elementima kolekcije.
	 *  @throws NullPointerException ako je procesor p null
	 */
	default void processRemaining(Processor p) {
		Objects.requireNonNull(p);
		while(hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
