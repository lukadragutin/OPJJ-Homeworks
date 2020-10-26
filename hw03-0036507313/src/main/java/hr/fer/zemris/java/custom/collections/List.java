package hr.fer.zemris.java.custom.collections;

/**
 * Sucelje koje modelira metode za upravljanje kolekcijama koje su ujedno i liste
 * @author Luka Dragutin
 *
 */
public interface List extends Collection {

	/**
	 * Dohvaca element iz liste sa indeksa index
	 * @param index Indeks trazenog elementa
	 * @return Object na zadanom indeksu
	 */
	Object get(int index);
	
	/**
	 * Ubacuje element na traženu poziciju, ne brišući element 
	 * koji je bio na tom mjestu, a ostale pomiče za jedno mjesto unaprijed.
	 * @param value  Vrijednost koja se ubacuje na poziciju position
	 * @param position  pozicija na koju se ubacuje vrijednost value
	 */
	void insert(Object value, int position);
	
	
	/**
	 * Vraća indeks tražene vrijednosti ako je u polju.
	 * Odnosi se samo na prvu takvu vrijednost na koju naiđe.
	 * @param value Vrijednost koja se trazi
	 * @return int indeks trazene vrijednosti
	 */
	int indexOf(Object value);
	
	/**
	 *  Briše vrijednost na zadanom indeksu i pomiče
	 * sve elemente iza obrisane vrijednosti za jedno 
	 * mjesto unatrag
	 * @param index indeks elementa koji se brise
	 */
	void remove(int index);
}
