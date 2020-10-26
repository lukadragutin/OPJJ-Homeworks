package hr.fer.zemris.java.custom.collections;

/**
 * Razred koji modelira stog i metode za upravljanje njime
 * @author Luka Dragutin
 *
 */
public class ObjectStack<T> {
	
	/** {@link ArrayIndexedCollection} koji predstavlja stog*/
	private ArrayIndexedCollection<T> stack;
	
	public ObjectStack() {
		stack = new ArrayIndexedCollection<T>();
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
	public void push(T value) {
		stack.add(value);
	}
	
	/**
	 * Vraća element sa vrha stoga briše ga.
	 * Složenosti 1.
	 * @throws EmptyStackException ako je stog prazan
	 * @return Object sa vrha stoga
	 */
	public T pop() {
		T temp = peek();
		stack.remove(stack.size() - 1);
		return temp;
	}
	
	/**
	 * Samo vraća element sa vrha stoga, ne briše ga
	 * @throws EmptyStackException ako je stog prazan
	 * @return
	 */
	public T peek() {
		if(stack.isEmpty()) {
			throw new EmptyStackException();
		}
		T temp = stack.get(stack.size() - 1);
		return temp;
	}
	
	/**
	 * Briše sve elemente sa stoga
	 */
	public void clear() {
		stack.clear();
	}
}
