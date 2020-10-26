package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Element koji ima read-only double varijablu value
 * @author Luka Dragutin
 *
 */
public class ElementConstantDouble extends Element {

	/**
	 * double vrijednost {@link ElementConstantDouble} objekta
	 */
	private double value;

	/**
	 * Stvara novi primjerak {@link ElementConstantDouble} sa 
	 * vrijednosti value
	 * @param value vrijednost koja se pohranjuje u objekt
	 * @throws NullPointerException ako je value null
	 */
	public ElementConstantDouble(double value) {
		super();
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * @return double vrijednost objekta
	 */
	public double getValue() {
		return value;
	}
	/**
	 * Vraca double vrijednost objekta kao string
	 */
	@Override
	public String asText() {
		return String.valueOf(value); 
	}
	
}
