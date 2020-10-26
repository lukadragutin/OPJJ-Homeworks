package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
/**
 * Dvostruko ulančana lista čvorova koji imaju referencu na 
 * prijašnji i idući čvor te vrijednost T
 * Objekt ima referencu na prvog i zadnjeg člana liste, te varijablu size
 * koja čuva broj zapisa u listi.
 * Lista može spremati duplikate, ali ne i null vrijednosti
 * Konstruktor moze primiti referencu na neku drugu kolekciju iz klase 
 * Collection te kopirati elemente iz iste.
 * @param <T> Tip elemenata u listi
 * @author Luka Dragutin
 *@version 1.0  
 */

public class LinkedListIndexedCollection<T> implements List<T> {
	
	/** Klasa koja opisuje strukturu jednog čvora liste
	 * Konstruktor prima vrijednost čvora, T value
	 */
	private static class ListNode<T> {
		
		/** referenca na člana ispred*/
		ListNode<T> previous;
		
		/** referenca na idućeg člana*/
		ListNode<T> next;
		
		/** vrijednost koju čvor posjeduje*/
		T value;
		
		public ListNode(T value) {
			this.value = value;
		}

		/**
		 * @return ListNode<T> prijašnji čvor {@link LinkedListIndexedCollection}
		 */
		@SuppressWarnings("unused")
		public ListNode<T> getPrevious() {
			return previous;
		}

		/**
		 * @return ListNode<T> idući čvor {@link LinkedListIndexedCollection}
		 */
		@SuppressWarnings("unused")
		public ListNode<T> getNext() {
			return next;
		}

		/**
		 * @return T vrijednost čvora
		 */
		public T getValue() {
			return value;
		}
		
		
	}
	
	/** Veličina liste*/
	private int size;
	
	/** Referenca na prvog člana liste*/
	private ListNode<T> first;
	
	/** Referenca na zadnjeg člana liste*/
	private ListNode<T> last;
	
	private long modificationCount;
	
	/** Postavlja first i last na null, a size na 0 */
	public LinkedListIndexedCollection() {
		first = last = null;
	}
	
	/**
	 * Inicijalizira listu te kopira elemente iz
	 * kolekcije other u nju
	 * @param other Kolekcija koju kopiramo
	 */
	public LinkedListIndexedCollection(Collection<? extends T> other) {
		this();
		addAll(other);
	}
	
	
	
	/**
	 * Getter za size
	 * @return int size od {@link LinkedListIndexedCollection}
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Getter za prvi čvor liste
	 * @return {@link ListNode} prvi čvor {@link LinkedListIndexedCollection} 
	 */
	public ListNode<T> getFirst() {
		return first;
	}

	/**
	 * Getter za zadnji čvor liste
	 * @return {@link ListNode} zadnji čvor {@link LinkedListIndexedCollection}
	 */
	public ListNode<T> getLast() {
		return last;
	}

	/**Dodaje novi element vrijednosti T value na kraj liste uz složenost (1)
	 * Ne prihvaća null vrijednosti
	 * @throws NullPointerException za null argumente
	 */
	@Override
	public void add(T value) {
		if(value == null) {
			throw new NullPointerException("Can't add null value to LinkedListIndexedCollection");
		}
		if(size == 0) {
			first = last = new ListNode<>(value);
		}
		else {
			ListNode<T> temp = new ListNode<>(value);
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
	 * @return Čvor {@link ListNode} na indeksu <code>index</code>
	 */
	private ListNode<T> getNode(int index) {
		if(index < size/2) {
			ListNode<T> temp = first;
			for(int i = 1; i <= index; i++) {
				temp = temp.next;
			}
			return temp;
		}
		else {
			ListNode<T> temp = last;
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
	 * @throws IndexOutOfBoundsException ako je index veći ili manji od dozvoljenog
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
	public void insert(T value, int position) {
		if(position < 0 || position > size) {
			throw new IndexOutOfBoundsException(position);
		}
		add(last.value);
		ListNode<T> temp = last.previous;
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
		ListNode<T> temp = first;
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
	 * @throws IndexOutOfBoundsException Ako je indeks izvan dozvoljenog raspona
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
		ListNode<T> temp = getNode(index);
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
	 * Metoda prvo pronalazi element sa zadanom vrijednošću,
	 * a potom ga briše.
	 * @param value Tražena vrijednost koju brišemo
	 * @return <code>true</code> ako je pronađena i izbrisana vrijednost
	 * value, <code>false</code> inače.
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
	 * Stvara i vraća polje veličine
	 * varijable {@link size} tipa T.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T[] toArray() {
		T[] array =(T[]) new Object[size];
		ListNode<T> temp = first;
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
	public ElementsGetter<T> createElementsGetter() {
		return new ElementsGetterLinkedList<>(this);
	}
	/**
	 * Privatna klasa ciji primjerak se stvara kad se pozove
	 * metoda {@link createElementsGetter}
	 * Ponasa se kao iterator, na zahtjev pozivatelja dohvaca jedan po 
	 * jedan element iz kolekcije.
	 * @author Luka Dragutin
	 *
	 */
	private static class ElementsGetterLinkedList<E> implements ElementsGetter<E> {
		
		/**
		 * Referenca na iduci clan kolekcije
		 */
		private ListNode<E> next;
		
		/**
		 * Referenca na primjerak kolekcije
		 */
		private LinkedListIndexedCollection<E> c;
		
		/**
		 * Broj promjena nad kolekcijom u trenutku stvaranja kolekcije
		 */
		private long savedModificationCount;
		
		/**
		 * Stvara {@link ElementsGetterLinkedList} za kolekciju predanu kao argument
		 * i pohranjuje vrijednost njezinog modificationCount-a
		 * @param c Kolekcija tipa {@link LinkedListIndexedCollection} ciji iterator nastaje
		 */
		protected ElementsGetterLinkedList (LinkedListIndexedCollection<E> c) {
			next = c.first;
			this.c = c;
			savedModificationCount = c.modificationCount;
		}
		/**
		 * @throws ConcurrentModificationException Ako je od nastanka iteratora bilo promjena nad kolekcijom
		 * {@link NoSuchElementException} Ako je iterator dosao do kraja kolekcije, odnosno nema vise elemenata.
		 */
		@Override
		public E getNextElement() {
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
