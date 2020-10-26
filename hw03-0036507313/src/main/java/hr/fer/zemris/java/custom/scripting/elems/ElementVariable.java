package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementVariable extends Element {

	private String name;
	
	public ElementVariable(String name) {
		this.name = Objects.requireNonNull(name);
	}
	
	@Override
	public String asText() {
		return name;
	}
}
