package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Razred predstavlja tablicu raspršenog adresiranja
 * koja omogućava pohranu uređenih parova (ključ, vrijednost).
 * Ključevi su jedinstveni dok se vrijednosti mogu ponavljati.
 * Jedan slot u tablici je modeliran javnim ugniježđenim razredom {@link TableEntry}.
 *
 * @param <K> Parametar K je tip ključa.
 * @param <V> Parametar V je tip vrijednosti.
 * @author Luka Dragutin
 */
public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/**
	 * Tablica rasprsenog adresiranja
	 */
	private TableEntry<K, V>[] table;
	
	/**
	 * Broj uredenih parova u mapi
	 */
	private int size;
	
	/**
	 * Broj izmjena nad mapom od stvaranja
	 */
	private long modificationCount;
	
	/**
	 * Broj zauzetih slotova tablice
	 */
	private int occupancy;
	
	/**
	 * Velicina alocirane tablice pri pozivu defaultnog kontruktora
	 */
	private final static int DEFAULT_CAPACITY = 16;
	
	/**
	 * Postotak popunjenosti kod kojeg dolazi do realokacije
	 */
	private final static double MAX_OCCUPANCY = 0.75;
	
	/**
	 * Defaultni konstruktor koji alocira tablicu
	 * raspršenog adresiranja velicine {@link DEFAULT_CAPACITY}
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Konstruktor alocira tablicu raspršenog adresiranja
	 * velicine najblize potencije broja 2 predanom argumentu
	 * @param capacity Velicina tablice koju alociramo, uzima se
	 * najbliza gornja potencija broja 2
	 * @throws IllegalArgumentException Ako je capacity manji od 1
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if(capacity < 1) {
			throw new IllegalArgumentException("Capacity has to be greater than 0!");
		}
		table = (TableEntry<K, V>[]) new TableEntry[getNextPotention(capacity)];
	}
	
	
	/**
	 * Pomocna metoda za dohvat najblize gornje potencije
	 * broja 2 predanog argumenta
	 * @param x Broj kojem trazimo najblizu potenciju broja 2
	 * @return int Prvu iducu vecu potenciju broja 2
	 */
	private int getNextPotention(int x) {
		int y = 1;
		while(y <= x) {
			y *= 2;
		}
		return y;
	}
	
	/**
	 * Provjerava je li mapa prazna
	 * @return boolean <code>true</code> ako je
	 * prazna <code>false</code> inace
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Vraca velicinu mape, clansku varijablu size
	 * @return int velicinu mape
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Ubacuje vrijednost <code>value</code> pod
	 * kljucem <code>key</code> u tablicu raspršenog adresiranja.
	 * U slučaju da neka vrijednost pod istim ključem već postoji,
	 * prebrisuje staru novom vrijednošću.
	 * @param key Kljuc uredenog para
	 * @param value Vrijednost uredenog para
	 * @throws NullPointerException ako je ključ <code>null</code>
	 */
	public void put(K key, V value) {
		int index = hash(Objects.requireNonNull(key));
		if(table[index] != null) {
			var temp = getEntry(index,key);
			if(temp != null) {
				temp.setValue(value);
				modificationCount++;
			}
			else {
				addToList(new TableEntry<>(key, value, null), index, table);
				modificationCount++;
				size++;
			}
		}
		else {
			if((occupancy + 1) / (double) table.length > MAX_OCCUPANCY) {
				reallocate();
				index = hash(key);
			}
			table[index] = new TableEntry<K, V>(key, value, null);
			size++;
			occupancy++;
			modificationCount++;
		}
	}
	
	/**
	 * Pomoćna metoda koja udvostručava kapacitet
	 * tablice ako dode do prevelikog popunjenja
	 */
	@SuppressWarnings("unchecked")
	private void reallocate() {
		TableEntry<K, V>[] temp = (TableEntry<K, V>[]) new TableEntry[table.length*2];
		for(var e : table) {
			while(e != null) {
				int index = Math.abs(e.getKey().hashCode()) % temp.length;
				addToList(e, index, temp);
				e = e.next;
			}
		}
		table = temp;
	}
	
	/**
	 * Pomoćna metoda za dohvat uređenog para
	 * pod zadanim ključem
	 * @param index Indeks u tablici na kojem se mjestu nalazi
	 * @param key Ključ koji tražimo
	 * @return {@link TableEntry} Uređen par pod zadanim ključem,
	 * ili <code>null</code> ako nema te vrijednosti
	 */
	private TableEntry<K, V> getEntry(int index, Object key){
		TableEntry<K, V> temp = table[index];
		while(temp != null) {
			if(temp.getKey().equals(key)) {
				return temp;
			}
			temp = temp.next;
		}
		return null;
	}
	
	/**
	 * Pomoćna metoda za dodavanje uređenog para
	 * na kraj podliste tablice raspršenog adresiranja
	 * @param entry Uređeni par koji dodajemo
	 * @param index Indeks na kojem se nalazi podlista u koju dodajemo
	 * @param table Tablica u koju dodajemo
	 */
	private void addToList(TableEntry<K,V> entry, int index, TableEntry<K, V>[] table) {
		var temp = table[index];
		if(temp == null) {
			table[index] = entry;
			return;
		}
		while(temp.next != null) {
			temp = temp.next;
		}
		temp.next = entry;
	}
	
	/**
	 * Vraća vrijednost spremljenu pod predanim ključem
	 * ili <code>null</code> ako ključa nema u tablici
 	 * @param key Ključ čiju vrijednost tražimo
	 * @return V Vrijednost uređenog para sa ključem
	 * <code>key</code> ili null ako nema takvog
	 */
	public V get(Object key) {
		var temp = getEntry(hash(key), key);
		if(temp != null) {
			return temp.getValue();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Provjerava sadrži li tablica uređeni 
	 * par sa ključem <code>key</code> 
	 * @param key Ključ koji tražimo
	 * @return <code>true</code> ako je ključ sadržan,
	 * a <code>false</code> inače
	 */
	public boolean containsKey(Object key) {
		if(key == null) {
			return false;
		}
		return getEntry(hash(key), key) != null;
	}
	
	/**
	 * Provjerava sadrži li tablica vrijednost <code>value</code>
	 * @param value Vrijednost koja se pretražuje
	 * @return <code>true</code> ako je vrijednost sadržana,
	 * a <code>false</code> inače
	 */
	public boolean containsValue(Object value) {
		for(TableEntry<K, V> e : table) {
			while(e != null) {
				if(e.getValue().equals(value)) {
					return true;
				}
				e = e.next;
			}
		}
		return false;
	}
	
	/**
	 * Uklanja jedan uređeni par iz tablice pod
	 * ključem <code>key</code>
	 * @param key ključ čiji uređeni par briše
	 */
	public void remove(Object key) {
		if(key == null) {
			return;
		}
		int index = hash(key);
		var temp = table[index];
		if(temp != null && temp.getKey().equals(key)) {
			if(temp.next != null) {
				table[index] = temp.next;
				modificationCount++;
				size--;
				return;
			}
			else {
				table[index] = null;
				modificationCount++;
				size--;
				occupancy--;
				return;
			}
		}
		while(temp != null && temp.next != null) {
			if(temp.next.getKey().equals(key)) {
				temp.next = temp.next.next;
				modificationCount++;
				size--;
				return;
			}
			temp = temp.next;
		}
	}
	
	/**
	 * Pomoćna metoda za 'hashanje' ključa
	 * @param key Ključ koji 'hashamo'
	 * @return 'hashirani' ključ
	 */
	private int hash(Object key) {
		return Math.abs(key.hashCode()) % table.length;
	}
	
	/**
	 * Briše sve elemente iz mape
	 */
	public void clear() {
		Arrays.setAll(table, (e) -> null);
		size = 0;
		modificationCount++;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(var e : table) {
			while(e != null) {
				sb.append(e.toString() + ", ");
				e = e.next;
			}
		}
		sb.replace(sb.length() - 2, sb.length(), "]");
		return sb.toString();
	}
	
	
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Iterator za obilazak elemenata mape.
	 * @author Luka Dragutin
	 *
	 */
	private class IteratorImpl implements Iterator<TableEntry<K, V>> {

		/**
		 * Index trenutnog slota tablice iteratora
		 */
		private int index;
		
		/**
		 * Trenutni uređeni par iteratora
		 */
		private TableEntry<K, V> current;
		
		/**
		 * Broj izmjena nad mapom u trenutku stvaranja iteratora
		 */
		private long savedModificationCount;
		
		/**
		 * Pomoćna varijabla za evidentiranje korištenja metode remove
		 */
		private boolean removed;
		
		/**
		 * Stvara novi primjerak iteratora
		 */
		public IteratorImpl() {
			savedModificationCount = modificationCount;
			index = 0;
			removed = true;
		}
		
		/**
		 * Vraća <code>true</code> ako iterator ima još elemenata, odnosno
		 * ako pri pozivu hasNext() nece baciti iznimku
		 * @throws ConcurrentModificationException Ako je od nastanka iteratora
		 * bilo promjena nad mapom
		 */
		@Override
		public boolean hasNext() {
			if(savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Changes to collection have been made!");
			}
			return index < table.length || current.next != null;
		}

		/**
		 * Dohvaća idući element iteratora ako on postoji,
		 * a ako ne postoji baca iznimku
		 * @throws NoSuchElementException Ako iterator nema više elemenata
		 * @return Idući element iteratora ako ga ima
		 */
		@Override
		public SimpleHashtable.TableEntry<K, V> next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			current = getNext();
			removed = false;
			return current;
		}
		
		/**
		 * Pomoćna metoda za dohvat idućeg elementa iteratora ako ga ima,
		 * ako ne onda vraća <code>null</code>
		 * @return
		 */
		private TableEntry<K, V> getNext() {
			if(current != null && current.next != null) {
				return current.next;
			}
			for(;index < table.length;index++) {
				var temp = table[index];
				if(temp != null) {
					index++;
					return temp;
				}
			}
			return null;
		}
		
		/**
		 * Briše posljednje iterirani član mape,
		 * ako se između poziva metode ne pozove next(), baca iznimku
		 * @throws ConcurrentModificationException Ako je od nastanka iteratora 
		 * bilo izmjena nad mapom
		 * @throws IllegalStateException Ako se između poziva ove metode ne
		 * pozove metoda next()
		 */
		@Override
		public void remove() {
			if(savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Changes to collection have been made!");
			}
			if(removed) {
				throw new IllegalStateException("Must call the next method first!");
			}
			SimpleHashtable.this.remove(current.getKey());
			removed = true;
			savedModificationCount++;
		}
		
	}

	/**
	 * Javna ugniježđena klasa koja predstavlja
	 * uređeni par(K ključ, V vrijednost).
	 * Koristi se u tablici raspršenog adresiranja za pohranu vrijednosti
	 * preko ključeva za identifikaciju. Nudi implementaciju
	 * jednostruko vezane liste za pohranjivanje više uređenih parova
	 * u jedan slot tablice.
	 * @author Luka Dragutin
	 * @param <K> Tip ključa uređenog para
	 * @param <V> Tip vrijednosti uređenog para
	 */
	public static class TableEntry<K,V> {

		/**
		 * Ključ uređenog para
		 */
		private K key;
		
		/**
		 * Vrijednost uređenog para
		 */
		private V value;
		
		/**
		 * Referenca na idućeg člana ulančane liste
		 */
		TableEntry<K,V> next;
		
		/**
		 * Stvara novi primjerak klase {@link TableEntry}
		 * @param key Ključ uređenog para
		 * @param value Vrijednost uređenog para
		 * @param next Pokazivač na idući uređeni par u listi
		 * @throws NullPointerException ako je ključ <code>null</code>
		 */
		public TableEntry(K key, V value, TableEntry<K,V> next) {
			this.key = Objects.requireNonNull(key);
			this.value = value;
			this.next = next;
		}
		
		public K getKey() {
			return key;
		}
		
		public V getValue() {
			return value;
		}
		
		public void setValue(V value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(key, value);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof TableEntry))
				return false;
			TableEntry<K, V> other = (TableEntry<K, V>) obj;
			return Objects.equals(key, other.key) && Objects.equals(value, other.value);
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}
		
		 
	}
	
	
}
