package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Razred koji definira opće metode nad kolekcijama, 
 * ali ne nudi konkretne implementacije
 * @author Luka
 *
 */
public interface Collection {

	/**
	 * Obavještava pozivatelja ako je kolekcija prazna
	 * @return boolean <Code>true</code> ako je prazna, <code>false</code> inace.
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 *  Vraća veličinu kolekcije 
	 */
	int size();
	
	/**
	 * Dodaje novi element s vrijednosti Value u kolekciju
	 * @param value Vrijednost koja se dodaje
	 */
	void add(Object value);
	
	/**
	 * Provjerava je li vrijednost sadržana u kolekciji i 
	 * o tom obavještava pozivatelja.
	 * @param value -> Tražena vrijednost.
	 * @return boolean -> true ako sadrži value, false inače.
	 */
	boolean contains(Object value);
	
	/**
	 * Ako je vrijednost u kolekciji uklanja ju te vraća true,
	 * ako nije vraća false
	 * @param value
	 * @return
	 */
	boolean remove(Object value);
	
	/**
	 * Vraća elemente kolekcije u obliku polja objekata
	 * klase Object
	 * @return Object[] Polje objekata nastalo od elemenata kolekcije
	 */
	 Object[] toArray();
	
	/**
	 * Obavlja radnju zadanu varijablom processor
	 * nad svim elementima kolekcije
	 * @param processor Definira postupak koji će se 
	 * obaviti nad elementima kolekcije
	 */
	 default void forEach(Processor processor) {
		 ElementsGetter getter = this.createElementsGetter();
		 getter.processRemaining(processor);
	 }
	
	/**
	 * Kopira sve elemente iz kolekcije other i dodaje ih
	 * @throws NullPointerException ako je kolekcija other null
	 * @param other Kolekcija iz koje kopiramo elemente
	 */
	default void addAll(Collection other) {
		Objects.requireNonNull(other);
		class CollectionProcessor implements Processor {

			public void process(Object other) {
				add(other);
			}
		}
		
		other.forEach(new CollectionProcessor());
	}
	
	/**
	 * Briše sve elemente iz kolekcije
	 */
	void clear();
	
	ElementsGetter createElementsGetter();
	
	default void addAllSatisfying(Collection col, Tester tester) {
		ElementsGetter getter = col.createElementsGetter();
		while(getter.hasNextElement()) {
			Object temp = getter.getNextElement();
			if(tester.test(temp)) {
				this.add(temp);
			}
		}
	}

}

