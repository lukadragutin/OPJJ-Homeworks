package hr.fer.zemris.java.custom.collections;

/**
 * Razred koji emitira ponašanje stoga
 * koristeći postojeće implementacije dvostruko povezane
 * liste samo ih maskirajući u metode koje korisnik veže uz 
 * operacije sa stogom.
 * @author Luka Dragutin
 *
 */
public class ObjectStack {
	
	/** Dvostruko ulančana lista koja "glumi" stog*/
	private LinkedListIndexedCollection stack;
	
	public ObjectStack() {
		stack = new LinkedListIndexedCollection();
	}
	
	/**
	 * Provjerava je li stog prazan
	 * @return boolean
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	/** @return int velicinu stoga */
	public int size() {
		return stack.size();
	}
	
	/**
	 * Stavlja element na vrh stoga.
	 * Složenosti 1.
	 * @param value
	 */
	public void push(Object value) {
		stack.add(value);
	}
	
	/**
	 * Vraća element sa vrha stoga briše ga.
	 * Složenosti 1.
	 * @throws EmptyStackException ako je stog prazan
	 * @return Object sa vrha stoga
	 */
	public Object pop() {
		Object temp = peek();
		stack.remove(stack.size() - 1);
		return temp;
	}
	
	/**
	 * Samo vraća element sa vrha stoga, ne briše ga
	 * @throws EmptyStackException ako je stog prazan
	 * @return
	 */
	public Object peek() {
		if(stack.isEmpty()) {
			throw new EmptyStackException();
		}
		Object temp = stack.get(stack.size() - 1);
		return temp;
	}
	
	/**
	 * Briše sve elemente sa stoga
	 */
	public void clear() {
		stack.clear();
	}
}
