package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Čvor koji modelira dokument
 * @author Luka Dragutin
 *
 */
public class DocumentNode extends Node {

	/**
	 * Javlja posjetitelju svoj razred
	 * @param visitor Posjetitelj
	 */
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
