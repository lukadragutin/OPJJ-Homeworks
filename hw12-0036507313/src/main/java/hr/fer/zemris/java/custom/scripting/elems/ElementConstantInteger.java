package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Element koji ima read-only int varijablu value
 * @author Luka
 *
 */
public class ElementConstantInteger extends Element {

	/**
	 * integer vrijednost {@link ElementConstantInteger} objekta
	 */
	private int value;

	/**
	 *  Stvara novi primjerak {@link ElementConstantInteger} sa 
	 * vrijednosti value
	 * @param value vrijednost koja se pohranjuje u objekt
	 * @throws NullPointerException ako je value null
	 */
	public ElementConstantInteger(int value) {
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * @return int vrijednost objekta
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 *  Vraca int vrijednost objekta kao string
	 */
	@Override
	public String asText() {
		return String.valueOf(value);
	}
	
	
}
