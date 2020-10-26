package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

public class TextNode extends Node {

	private String text;

	public TextNode(String text) {
		super();
		this.text = Objects.requireNonNull(text);
	}

	/**
	 * @return String clansku varijablu text	 */
	public String getText() {
		return text;
	}
}
