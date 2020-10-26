package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementString extends Element {

	private String value;

	public ElementString(String value) {
		super();
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * @return the value
	 */
	@Override
	public String asText() {
		return value;
	}
	
	
}
