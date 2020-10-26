package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Razred predstavlja listu koja omogučuje spremanje uređenih
 * parova(ključ, vrijednost), tj. ponaša se kao mapa.
 * Ključevi su jedinstveni dok se vrijednosti mogu ponavljati.
 * Jedan član liste modeliran je privatnim ugniježđenim razredom
 * koji predstavlja uređeni par
 * @author Luka Dragutin
 * 
 * @param <K> Tip ključa
 * @param <V> Tip vrijednosti
 */
public class Dictionary<K,V> {

	/**
	 * Lista u koju se spremaju uređeni parovi
	 */
	private ArrayIndexedCollection<Entry<K,V>> array;
	
	/**
	 * Stvara novi primjerak razreda {@link Dictionary} alocirajuci
	 * novu listu za spremanje uređenih parova
	 */
	public Dictionary() {
		array = new ArrayIndexedCollection<>();
	}
	
	/**
	 * Provjerava je li mapa prazna
	 * @return boolean <code>true</code> ako je
	 * prazna <code>false</code> inace
	 */
	public boolean isEmpty() {
		return array.isEmpty();
	}
	
	/**
	 * Vraća veličinu mape, člansku varijablu size
	 * @return int veličinu mape
	 */
	public int size() {
		return array.size();
	}
	
	/**
	 * Briše sve elemente
	 */
	public void clear() {
		array.clear();
	}
	
	/**
	 * Ubacuje vrijednost <code>value</code> pod
	 * kljucem <code>key</code> u listu.
	 * U slučaju da neka vrijednost pod istim ključem već postoji,
	 * prebrisuje staru novom vrijednošću.
	 * @param key Ključ uređenog para.
	 * @param value Vrijednost uređenog para.
	 * @throws NullPointerException ako je ključ <code>null</code>
	 */
	public void put(K key, V value) {
		Entry<K, V> entry = new Entry<>(key, value);
		if(isEmpty()) {
			array.add(entry);
		}
		int index = getKeyIndex(key);
		if(index != -1) {
			array.remove(index);
			array.insert(entry, index);
			return;
		}
		array.add(entry);
	}
	
	/**
	 * Vraća vrijednost spremljenu pod predanim ključem
	 * ili <code>null</code> ako ključa nema u listi
 	 * @param key Ključ čiju vrijednost tražimo
	 * @return V Vrijednost uređenog para sa ključem
	 * <code>key</code> ili null ako nema takvog
	 */
	public V get(Object key) {
		int index = getKeyIndex(key);
		if(index != -1) {
			return array.get(index).value;
		}
		return null;
	}
	
	/**
	 * Pomoćna metoda za dohvat indeksa
	 * uređenog para sa ključem <code>key</code>
	 * @param key Ključ koji tražimo
	 * @return int indeks uređenog para sa ključem <code>key</code>
	 *  ili -1 ako nema takvog uređenog para
	 */
	private int getKeyIndex(Object key) {
		ElementsGetter<Entry<K, V>> getter = array.createElementsGetter();
		int index = 0;
		while(getter.hasNextElement()) {
			Entry<K, V> entry = getter.getNextElement();
			if(entry.key.equals(key)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	/**
	 * Privatna ugniježđena klasa koja predstavlja
	 * uređeni par(K ključ, V vrijednost).
	 * Koristi se u listi za pohranu vrijednosti
	 * preko ključeva za identifikaciju.
	 * @param <K> Tip ključa
	 * @param <V> Tip vrijednosti
	 */
	private static class Entry<K,V> {
		
		/**
		 * Ključ uređenog para
		 */
		private K key;
		
		/**
		 * Vrijednost uređenog para
		 */
		private V value;
		
		/**
		 * Stvara novi primjerak razreda {@link Entry}
		 * sa ključem <code>key</code> i vrijednošću <code>value</code>.
		 * @param key Ključ uređenog para
		 * @param value Vrijednosti uređenog para
		 * @throws NullPointerException ako je ključ <code>null</code>
		 */
		protected Entry(K key, V value) {
			this.key = Objects.requireNonNull(key);
			this.value = value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(key, value);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Entry))
				return false;
			Entry other = (Entry) obj;
			return Objects.equals(key, other.key) && Objects.equals(value, other.value);
		}
		
		
	}
}
