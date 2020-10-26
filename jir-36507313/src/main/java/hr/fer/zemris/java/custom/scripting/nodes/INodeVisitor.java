package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Sucelje koje modelira posjetitelja cvorova {@link Node} koji 
 * ovisno o zeljenoj namjeni, implementira postupanje sa svakim od tipova
 * razreda {@link Node}
 * @see Node
 * @author Luka Dragutin
 *
 */
public interface INodeVisitor {

	/**
	 * Postupak kad je posjećeni {@link Node} primjerak
	 * razreda {@link TextNode}
	 * @param node Posjećeni čvor
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Postupak kad je posjećeni {@link Node} primjerak
	 * razreda {@link ForLoopNode}
	 * @param node Posjećeni čvor
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Postupak kad je posjećeni {@link Node} primjerak
	 * razreda {@link EchoNode}
	 * @param node Posjećeni čvor
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Postupak kad je posjećeni {@link Node} primjerak
	 * razreda {@link DocumentNode}
	 * @param node Posjećeni čvor
	 */
	public void visitDocumentNode(DocumentNode node);
	
	public void visitTimeNode(TimeNode node);
}
