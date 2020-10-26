package hr.fer.zemris.java.custom.collections;

/**
 * Razred koji definira opće metode nad kolekcijama, 
 * ali ne nudi konkretne implementacije
 * @author Luka
 *
 */
public class Collection {

	/**
	 * Obavještava pozivatelja ako je kolekcija prazna
	 * @return boolean
	 */
	public boolean isEmpty() {
		if(size() > 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/** Vraća veličinu kolekcije */
	public int size() {
		return 0;
	}
	
	/**
	 * Dodaje novi element s vrijednosti Value u kolekciju
	 * @param value Vrijednost koja se dodaje
	 */
	public void add(Object value) {
		
	}
	
	/**
	 * Provjerava je li vrijednost sadržana u kolekciji i 
	 * o tom obavještava pozivatelja.
	 * @param value -> Tražena vrijednost.
	 * @return boolean -> true ako sadrži value, false inače.
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Ako je vrijednost u kolekciji uklanja ju te vraća true,
	 * ako nije vraća false
	 * @param value
	 * @return
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Vraća elemente kolekcije u obliku polja objekata
	 * klase Object
	 * @return Object[]
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Obavlja radnju zadanu varijablom processor
	 * nad svim elementima kolekcije
	 * @param processor Definira postupak koji će se 
	 * obaviti nad elementima kolekcije
	 */
	public void forEach(Processor processor) {
		
	}
	
	/**
	 * Kopira sve elemente iz kolekcije other i dodaje ih
	 * @param other Kolekcija iz koje kopiramo elemente
	 */
	public void addAll(Collection other) {
		class CollectionProcessor extends Processor {
			@Override
			public void process(Object other) {
				add(other);
			}
		}
		
		other.forEach(new CollectionProcessor());
	}
	
	/**
	 * Briše sve elemente iz kolekcije
	 */
	public void clear() {
		
	}
}

