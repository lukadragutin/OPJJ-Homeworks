package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementOperator extends Element {

	private String symbol;

	public ElementOperator(String symbol) {
		super();
		this.symbol = Objects.requireNonNull(symbol);
	}

	@Override
	public String asText() {
		return symbol;
	}
	
	
	
}
