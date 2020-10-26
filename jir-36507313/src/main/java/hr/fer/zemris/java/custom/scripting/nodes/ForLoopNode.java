package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Razred koji pohranjuje tag naredbu for u obliku {@link Node}
 * Prima 1 varijablu i 2 ili 3 argumenta u obliku int, double ili String
 * Varijable i argumente pohranjuje kao primjerke razreda {@link Element}
 * @author Luka Dragutin
 *
 */
public class ForLoopNode extends Node {

	/**
	 * Svojstva varijable u for petlji
	 */
	private ElementVariable variable;
	
	/**
	 * Broj od kojeg se for petlja pocinje izvrsavati
	 */
	private Element startExpression;
	
	/**
	 * Broj do kojeg se for petlja izvrsava
	 */
	private Element endExpression;
	
	/**
	 * Korak varijable kroz iteracije, moze biti null
	 */
	private Element stepExpression;
	
	/**
	 * Stvara novi primjerak klase {@link ForLoopNode}
	 * @throws NullPointerException za null argumente variable, startExpression, endExpression
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		super();
		this.variable = Objects.requireNonNull(variable);
		this.startExpression = Objects.requireNonNull(startExpression);
		this.endExpression = Objects.requireNonNull(endExpression);
		this.stepExpression = stepExpression;
	}

	/**
	 * @return {@link ElementVariable} clansku varijablu variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * @return {@link Element} clansku varijablu getStartExpression
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * @return {@link Element} clansku varijablu getEndExpression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * @return {@link Element} clansku varijablu getStepExpression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	/**
	 * Javlja posjetitelju svoj razred
	 * @param visitor Posjetitelj
	 */
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
}
