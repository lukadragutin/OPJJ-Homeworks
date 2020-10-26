package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptTokenType;

/**
 * Razred koji cuva argumente funkcije '='
 * @author Luka Dragutin
 *
 */
public class EchoNode extends Node {

	/**polje argumenata funkcije tipa {@link Element}
	 */
	private Element[] elements;

	/**Stvara novi primjerak sa zadanim argumentima
	 * @param elements argumenti koji se spremaju u objekt
	 * @throws NullPointerException ako je elements null
	 */
	public EchoNode(Element[] elements) {
		this.elements = Objects.requireNonNull(elements);
	}

	/**
	 * @return Element[] Polje argumenata
	 */
	public Element[] getElements() {
		return elements;
	}
}
