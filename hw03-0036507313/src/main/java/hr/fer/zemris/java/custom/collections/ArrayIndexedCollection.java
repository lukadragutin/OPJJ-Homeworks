package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Polje sadržano od elemenata Object uz varijablu size koja
 * bilježi broj elemenata u kolekciji.
 * Može sadržavati duplikate, ali ne i null vrijednosti.
 * Konstruktorima se predaje željena veličina polja i/ili kolekcija
 * koju želimo kopirati u polje
 * Default konstruktor postavlja polje na veličinu {@link DEFAULT_CAPACITY}
 * @author Luka Dragutin
 * version 1.0
 *
 */
public class ArrayIndexedCollection implements List {
	
	/** veličina polja */
	private int size;
	
	/** Polje elemenata(vrijednosti) */
	private Object[] elements;
	
	/**
	 * Broj izmjena sadrzaja kolekcije od njezina stvaranja
	 */
	private long modificationCount;
	
	/** Kapacitet polja pri pozivanju default konstruktora*/
	private static final int DEFAULT_CAPACITY = 16;
	
	/** Minimalni kapacitet potreban za stvaranje novog primjerka razreda*/
	private static final int MIN_CAPACITY = 1;
	
	/**
	 *  Alocira polje velicine initialCapacity koji mora
	 *  veći od {@link MIN_CAPACITY} inače baca {@link IllegalArgumentException} 
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity must be greater than " + MIN_CAPACITY);
		}
		elements = new Object[initialCapacity];
	}
	
	/** Alocira polje velicine {@link DEFAULT_CAPACITY} */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Alocira polje veličine initialCapacity i kopira kolekciju
	 * other u njega. Ako je predana kolekcije većeg kapaciteta od 
	 * argumenta {@link initialCapacity} onda se polje alocira na veličinu 
	 * predane kolekcije
	 * @param initialCapacity Mora biti veći od {@link MIN_CAPACITY} inače 
	 * baca {@link IllegalArgumentException}
	 * @param other Kolekcija koja se kopira u polje
	 */
	public ArrayIndexedCollection(int initialCapacity, Collection other) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity must be greater than " + MIN_CAPACITY);
		}
		if (other.size() > initialCapacity) {
			elements = new Object[other.size()];
		}
		else {
			elements = new Object[initialCapacity];
		}
		addAll(other);
	}
	
	/**
	 * Alocira polje veličine {@link DEFAULT_CAPACITY}, osim ako je 
	 * veličina kolekcije other veća od tog, onda se alocira polje veličine 
	 * kolekcije other, a ista se kopira u novoalocirano polje
	 * @param other Kolekcija ciji se sadrzaj kopira u novonastali {@link ArrayIndexedCollection}
	 */
	public ArrayIndexedCollection(Collection other) {
		this(DEFAULT_CAPACITY, other);
	}
	
	/**
	 * Dodaje element u polje ako ima mjesta, a ako 
	 * nema proširuje veličinu polja udvostručujući ga.
	 * Prosječna složenost je 1, osim kad ponestane mjesta,
	 * tj. treba realocirati polje, tad je n.
	 * @throws NullPointerException za vrijednost null
	 */
	@Override
	public void add(Object value) {
		if(value == null) {
			throw new NullPointerException("Null cannot be added into collection");
		}
		if(size == elements.length) {
			elements = expandArray(elements);
		}
		elements[size++] = value;
		modificationCount++;
	}
	
	/**
	 * Udvostrucava kapacitet polja koji cuva elemente kolekcije bez mijenjanja istih
	 * @param array Polje ciji se kapacitet udvostrucuje
	 * @return Object[] Polje udvostrucenog kapaciteta
	 */
	private Object[] expandArray(Object[] array) {
		Object[] temp = new Object[elements.length*2];
		System.arraycopy(elements, 0, temp, 0, elements.length);
		return temp;
	}
	
	/**
	 * Vraća element iz polja na traženom indeksu
	 * @param index -> može biti 0 - (size-1)
	 * @return Object na indeksu {@link index}
	 * @throws IndexOutOfBoundsException ako je index van dosega
	 */
	public Object get(int index) {
		if(index < 0 || index > (size-1)) {
			throw new IndexOutOfBoundsException(index);
		}
		return elements[index];
	}

	public void clear() {
		for(int i = 0; i < elements.length; i++) {
			elements[i] = null;
		}
		size = 0;
		modificationCount++;
	}

	public int size() {
		return size;
	}

	
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Ubacuje element na traženu poziciju, ne brišući element 
	 * koji je bio na tom mjestu, a ostale pomiče za jedno mjesto unaprijed.
	 * Prosječne je složenosti n/2
	 * @param value Vrijednost koja se ubacuje na poziciju position
	 * @param position -> može biti 0 - size, pozicija na koju se ubacuje vrijednost value
	 * @throws {@link IndexOutOfBoundsException} ako je {@link position} izvan dosega
	 * 			{@link NullPointerException} ako je value null vrijednost
	 */
	public void insert(Object value, int position) {
		Objects.requireNonNull(value);
		if(position < 0 || position > size) {
			throw new IndexOutOfBoundsException(position);
		}
		if(size == position) {
			add(value);
		}
		else {
			add(elements[size-1]);
			for(int i = size-1; i > position; i--) {
				elements[i] = elements[i-1];
			}
			elements[position] = value;
			modificationCount++;
		}
	}
	/**
	 * Vraća indeks tražene vrijednosti ako je u polju.
	 * Odnosi se samo na prvu takvu vrijednost na koju naiđe.
	 * Prosječna složenost je n/2
	 * @param value Vrijednost koja se pretrazuje
	 * @return int -> indeks tražene vrijednosti ako je u polju,
	 * a -1 inače
	 */
	public int indexOf(Object value) {
		if(value == null) {
			return -1;
		}
		for(int i = 0; i < size; i++) {
			if(elements[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * Briše vrijednost na zadanom indeksu i pomiče
	 * sve elemente iza obrisane vrijednosti za jedno 
	 * mjesto unatrag
	 * @param index -> može biti 0 - (size - 1)
	 * indeks elementa koji se brise iz kolekcije
	 * @throws IndexOutOfBoundsException ako je index izvan dosega
	 */
	public void remove(int index) {
		if(index < 0 || index > (size-1)) {
			throw new IndexOutOfBoundsException(index);
		}
		for(int i = index; i < (size - 1); i++) {
			elements[i] = elements [i+1];
		}
		elements[size - 1] = null;
		size--;
		modificationCount++;
	}
	
	
	public Object[] toArray() {
		Object[] array = new Object[size];
		System.arraycopy(elements, 0, array, 0, size);
		return array;
	}
	
	/**
	 * Pronalazi vrijednost sa {@link indexOf} i uklanja
	 * ju sa {@link remove}
	 * @param value vrijednost koja se uklanja
	 * @throws NullPointerException ako je value null
	 */
	public boolean remove(Object value) {
		Objects.requireNonNull(value);
		int index = indexOf(value);
		if(index < 0) {
			return false;
		}
		remove(index);
		return true;
	}
	

	@Override
	public ElementsGetter createElementsGetter() {
		return new ElementsGetterArray(this);
	}
	
	/**
	 * Privatna klasa koja implementira sucelje {@link ElementsGetter},
	 * koja sluzi za iteriranje po kolekciji
	 * @author Luka Dragutin
	 *
	 */
	private static class ElementsGetterArray implements ElementsGetter {
		
		/**
		 * indeks na kojem se iterator trenutno nalazi
		 */
		private int index;
		
		/**
		 * Broj izmjena nad kolekcijom u trenutku stvaranja iteratora
		 */
		private long savedModificationCount;
		
		/**
		 * Kopija reference na kolekciju ciji je ovo iterator
		 */
		private ArrayIndexedCollection c;
		
		/**
		 * Stvara iterator od primjerka kolekcije {@link ArrayIndexedCollection} c
		 * tako da sprema referencu na nju i njezin trenutni broj izmjena modificationCount
		 * @param c Kolekcija ciji je ovo iterator
		 */
		protected ElementsGetterArray(ArrayIndexedCollection c) {
			this.c = c;
			savedModificationCount = c.modificationCount;
		}
		
		/**
		 * <{@inheritDoc}
		 * @throws NoSuchElementException Ako je kolekcije prazna u trenutku pozivanja.
		 *  {@link ConcurrentModificationException} Ako su radene izmjene nad kolekcijom od stvaranja njegova {@link ElementsGetterArray}
		 */
		@Override
		public Object getNextElement() {
			if(c.modificationCount != savedModificationCount) {
				throw new ConcurrentModificationException("Kolekcija je mijenjana");
			}
			if(c.elements[index] == null) {
				throw new NoSuchElementException("Kolekcija je prazna.");
			}

			return c.elements[index++];
		}
		
		/**
		 * <{@inheritDoc}
		 * @throws ConcurrentModificationException Ako su radene izmjene nad kolekcijom od stvaranja njezina {@link ElementsGetterArray}
		 */
		@Override
		public boolean hasNextElement() {
			if(c.modificationCount != savedModificationCount) {
				throw new ConcurrentModificationException("Kolekcija je mijenjana");
			}
			return c.elements[index] != null;
		}
	}
}
