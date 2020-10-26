package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Sučelje koji definira opće metode nad kolekcijama.
 * @author Luka Dragutin
 *
 */
public interface Collection<T> {

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
	 * @param other Vrijednost koja se dodaje
	 */
	void add(T other);
	
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
	 * Vraća elemente kolekcije u obliku polja objekata klase T
	 * @return T[] Polje objekata tipa T nastalo od elemenata kolekcije
	 */
	T[] toArray();
	
	/**
	 * Obavlja radnju zadanu varijablom processor
	 * nad svim elementima kolekcije
	 * @param processor Definira postupak koji će se 
	 * obaviti nad elementima kolekcije
	 */
	 default void forEach(Processor<? super T> processor) {
		 ElementsGetter<T> getter = this.createElementsGetter();
		 getter.processRemaining(processor);
	 }
	
	/**
	 * Kopira sve elemente iz kolekcije other i dodaje ih
	 * @throws NullPointerException ako je kolekcija other null
	 * @param other Kolekcija iz koje kopiramo elemente
	 */
	default void addAll(Collection<? extends T> other) {
		Objects.requireNonNull(other);
		class CollectionProcessor<E> implements Processor<T> {

			public void process(T other) {
				add(other);
			}
		}
		
		other.forEach(new CollectionProcessor<T>());
	}
	
	/**
	 * Briše sve elemente iz kolekcije
	 */
	void clear();
	
	ElementsGetter<T> createElementsGetter();
	
	@SuppressWarnings("unchecked")
	default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
		ElementsGetter<?> getter = col.createElementsGetter();
		while(getter.hasNextElement()) {
			T temp = (T) getter.getNextElement();
			if(tester.test(temp)) {
				this.add(temp);
			}
		}
	}

}

