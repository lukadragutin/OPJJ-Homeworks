package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

public class TimeNode extends Node {

	private String text;

	public TimeNode(String text) {
		super();
		this.text = text;
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
		//visitor.visitTextNode(this);
	}
}
