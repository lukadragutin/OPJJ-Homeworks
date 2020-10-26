package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.*;

/**
 * Kontekst L-sustava gdje se pohranjuju
 * stanja "konjače"
 * @author Luka Dragutin
 */
public class Context {

	/**Stog na koji se spremaju stanja L-sustava*/
	private ObjectStack<TurtleState> stack;
	
	
	/**Stvara novi stog*/
	public Context() {
		super();
		stack = new ObjectStack<>();
		
		}
	

	/**
	 * Dohvaca trenutno stanje L-sustava sa stoga,
	 * bez uklanjanja istog
	 * @return Trenutno stanje "kornjače" L-sustava
	 * @throws EmptyStackException ako je stog prazan
	 */
	public TurtleState getCurrentState() {
		return stack.peek();
	}
	
	/**
	 * Sprema stanje iz argumenta na stog, te ono postaje
	 * novo trenutno stanje L-sustava
	 * @param state Novo trenutno stanje "kornjače" L-sustava koje
	 * se sprema na stog
	 */
	public void pushState(TurtleState state) {
		stack.push(state);
	}
	
	/**
	 * Uklanja trenutno stanje sa vrha stoga konteksta L-sustava
	 * @throws EmptyStackException ako je stog prazan
	 */
	public void popState() {
		stack.pop();
	}
}
