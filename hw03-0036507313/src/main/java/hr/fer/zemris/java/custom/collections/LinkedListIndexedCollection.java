package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
/**
 * Dvostruko ulančana lista čvorova koji imaju referencu na 
 * prijašnji i idući čvor te vrijednost Object
 * Objekt ima referencu na prvog i zadnjeg člana liste, te varijablu size
 * koja čuva broj zapisa u listi
 * Lista može spremati duplikate, ali ne i null vrijednosti
 * Konstruktor moze primiti referencu na neku drugu kolekciju iz klase 
 * Collection te kopirati elemente iz iste
 * @author Luka Dragutin
 *@version 1.0  
 */

public class LinkedListIndexedCollection implements List {
	
	/** Klasa koja opisuje strukturu jednog čvora liste
	 * Konstruktor prima vrijednost čvora, Object value
	 */
	private static class ListNode {
		
		/** referenca na člana ispred*/
		ListNode previous;
		
		/** referenca na idućeg člana*/
		ListNode next;
		
		/** vrijednost koju čvor posjeduje*/
		Object value;
		
		public ListNode(Object value) {
			this.value = value;
		}

		/**
		 * @return the previous ListNode in the {@link LinkedListIndexedCollection}
		 */
		public ListNode getPrevious() {
			return previous;
		}

		/**
		 * @return the next ListNode in the {@link LinkedListIndexedCollection}
		 */
		public ListNode getNext() {
			return next;
		}

		/**
		 * @return the value of the ListNode
		 */
		public Object getValue() {
			return value;
		}
		
		
	}
	
	/** Veličina liste*/
	private int size;
	
	/** Referenca na prvog člana liste*/
	private ListNode first;
	
	/** Referenca na zadnjeg člana liste*/
	private ListNode last;
	
	private long modificationCount;
	
	/** Postavlja first i last na null, a size na 0 */
	public LinkedListIndexedCollection() {
		first = last = null;
	}
	
	/**
	 * Inicijalizira listu te kopira elemente iz
	 * kolekcije other u nju
	 * @param other
	 */
	public LinkedListIndexedCollection(Collection other) {
		this();
		addAll(other);
	}
	
	
	
	/**
	 * Getter for size of the list
	 * @return the size of the {@link LinkedListIndexedCollection}
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Getter for the first element
	 * @return the first {@link ListNode} in the {@link LinkedListIndexedCollection} 
	 */
	public ListNode getFirst() {
		return first;
	}

	/**
	 * Getter for last element
	 * @return the last {@link ListNode} in the {@link LinkedListIndexedCollection}
	 */
	public ListNode getLast() {
		return last;
	}

	/**Dodaje novi element vrijednosti Object value na kraj liste uz složenost (1)
	 * Ne prihvaća null vrijednosti
	 * @throws NullPointerException za null argumente
	 */
	@Override
	public void add(Object value) {
		if(value == null) {
			throw new NullPointerException("Can't add null value to LinkedListIndexedCollection");
		}
		if(size == 0) {
			first = last = new ListNode(value);
		}
		else {
			ListNode temp = new ListNode(value);
			last.next = temp;
			temp.previous = last;
			last = temp;
		}
		size++;
		modificationCount++;
	}
	
	/**
	 *  Pomoćna metoda za dohvat elementa na traženom indeksu
	 * @param index
	 * @return
	 */
	private ListNode getNode(int index) {
		if(index < size/2) {
			ListNode temp = first;
			for(int i = 1; i <= index; i++) {
				temp = temp.next;
			}
			return temp;
		}
		else {
			ListNode temp = last;
			for(int i = size-2; i >= index; i--) {
				temp = temp.previous;
			}
			return temp;
		}
	}
	
	/**
	 * Vraća element koji se nalazi na traženom indeksu.
	 * Koristi se pomoćnom metodom getNode, a
	 * prosječna joj je složenost (n/2+1).
	 * @param index Može biti u rasponu od 0 do size-1, inače baca {@link IndexOutOfBoundsException}
	 * @return Object koji je na zadanom indeksu index
	 * @throws IndexOutOfBoundsException
	 */
	public Object get(int index) {
		if(index < 0 || index > (size-1)) {
			throw new IndexOutOfBoundsException(index);
		}
		return getNode(index).value;
	}
	
	@Override
	public void clear() {
		first = last = null;
		size = 0;
		modificationCount++;
	}
	
	/**
	 * Ubacuje element na zadanom indeksu, a ostale elemente 
	 * pomiče za jedno mjesto prema kraju.
	 * Prosječne je složenosti n.
	 * @param value -> vrijednost za ubaciti u listu
	 * @param position -> 0 - size, inače baca {@link IndexOutOfBoundsException}
	 */
	public void insert(Object value, int position) {
		if(position < 0 || position > size) {
			throw new IndexOutOfBoundsException(position);
		}
		add(last.value);
		ListNode temp = last.previous;
		for(int i = (size - 2); i > position; i--) {
			temp.value = temp.previous.value;
			temp = temp.previous;
		}
		temp.value = value;
		modificationCount++;
	}
	
	/**
	 * Pretražuje listu za zadanim objektom.
	 * Prosječne je složenosti n.
	 * @param value -> Tražena vrijednost.
	 * @return int -> Indeks traženog objekta ako ga pronađe, -1 inače.
	 */
	public int indexOf(Object value) {
		if(value == null) {
			return -1;
		}
		ListNode temp = first;
		for(int i = 0; i < size; i++) {
			if(temp.value.equals(value)) {
				return i;
			}
			temp = temp.next;
		}
		return -1;
	}
	/**
	 * Uklanja element sa zadanog indeksa i sve elemente koji su 
	 * bili iza njega pomiče za jedno mjesto prema početku.
	 * Za rubne elemente složenosti 1, n inače
	 * @param index -> Doseg 0 - (size - 1), inače baca {@link IndexOutOfBoundsException}.
	 */
	public void remove(int index) {
		if(index < 0 || index > (size-1)) {
			throw new IndexOutOfBoundsException(index);
		}
		if(index == 0) {
			first = first.next;
			size--;
			return;
		}
		if(index == (size-1)) {
			last = last.previous;
			size--;
			return;
		}
		ListNode temp = getNode(index);
		temp.previous.next = temp.next;
		temp.next.previous = temp.previous;
		temp = temp.next;
		--size;
		modificationCount++;
	}
	
	@Override
	public boolean contains(Object value) {
		int i = indexOf(value);
		if(i < 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Metoda prvo pronalazi element sa zadanom vrijednosti 
	 * sa metodom {@link indexOf}, a potom ga briše metodom
	 * {@link remove}
	 * 
	 */
	@Override
	public boolean remove(Object value) {
		int i = indexOf(value);
		if(i < 0) {
			return false;
		}
		remove(i);
		return true;
	}

	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Stvara i vraća polje Objecta veličine
	 * varijable {@link size}.
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		ListNode temp = first;
		for(int i = 0; i < size; i++) {
			array[i] = temp.value;
			temp = temp.next;
		}
		return array;
	}
	/**
	 * Metoda stvara i vraca {@link ElementsGetterLinkedList} 
	 * @return ElementsGetter iterator za kolekciju {@link LinkedListIndexedCollection}
	 */
	public ElementsGetter createElementsGetter() {
		return new ElementsGetterLinkedList(this);
	}
	/**
	 * Privatna klasa ciji primjerak se stvara kad se pozove
	 * metoda {@link createElementsGetter}
	 * Ponasa se kao iterator, na zahtjec pozivatelja dohvaca jedan po 
	 * jedan element iz kolekcije
	 * @author Luka
	 *
	 */
	private static class ElementsGetterLinkedList implements ElementsGetter {
		
		/**
		 * Referenca na iduci clan kolekcije
		 */
		private ListNode next;
		
		/**
		 * Referenca na primjerak kolekcije
		 */
		private LinkedListIndexedCollection c;
		
		/**
		 * Broj promjena nad kolekcijom u trenutku stvaranja kolekcije
		 */
		private long savedModificationCount;
		
		/**
		 * Stvara {@link ElementsGetterLinkedList} za kolekciju predanu kao argument
		 * i pohranjuje vrijednost njezinog modificationCount-a
		 * @param c Kolekcija tipa {@link LinkedListIndexedCollection} ciji iterator nastaje
		 */
		protected ElementsGetterLinkedList (LinkedListIndexedCollection c) {
			next = c.first;
			this.c = c;
			savedModificationCount = c.modificationCount;
		}
		/**
		 * @throws ConcurrentModificationException Ako je od nastanka iteratora bilo promjena nad kolekcijom
		 * {@link NoSuchElementException} Ako je iterator dosao do kraja kolekcije, odnosno nema vise elemenata.
		 */
		@Override
		public Object getNextElement() {
			if(c.modificationCount != savedModificationCount) {
				throw new ConcurrentModificationException("Kolekcija je mijenjana");
			}
			if(next == null) {
				throw new NoSuchElementException("Nema više elemenata u kolekciji.");
			}
			var temp = next;
			next = temp.next;
			return temp.getValue();
		}
		/**
		 * @throws ConcurrentModificationException  Ako je od nastanka iteratora bila promjena nad kolekcijom.
		 */
		@Override
		public boolean hasNextElement() {
			if(c.modificationCount != savedModificationCount) {
				throw new ConcurrentModificationException("Kolekcija je mijenjana");
			}
			return next != null;
		}
	}
}
