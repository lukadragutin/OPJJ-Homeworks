package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Element koji cuva ime funkcije
 * @author Luka Dragutin
 *
 */
public class ElementFunction extends Element {

	/**
	 * Ime funkcije
	 */
	private String name;

	/**
	 * @param name ime funkcije
	 * @throws NullPointerException ako je name null
	 */
	public ElementFunction(String name) {
		super();
		this.name = Objects.requireNonNull(name);
	}

	/**
	 * @return String imena funkcije
	 */
	@Override
	public String asText() {
		return name;
	}

	
}
