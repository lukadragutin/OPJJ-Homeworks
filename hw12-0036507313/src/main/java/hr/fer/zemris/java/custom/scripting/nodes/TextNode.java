package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * Čvor koji sadrži tekstualnu vrijednost
 * @author Luka Dragutin
 *
 */
public class TextNode extends Node {

	/**Tekstualna vrijednost čvora*/
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
	
	/**
	 * Javlja posjetitelju svoj razred
	 * @param visitor Posjetitelj
	 */
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
