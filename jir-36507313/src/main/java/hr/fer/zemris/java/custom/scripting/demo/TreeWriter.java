package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.nodes.TimeNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Ispisuje sadrzaj skripte koristeci implementaciju {@link INodeVisitor}
 * @author Luka Dragutin
 *
 */
public class TreeWriter {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Expected file path..");
			return;
		}

		String docBody;
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
		} catch (IOException e1) {
			System.out.println("File can't be read.");
			return;
		}
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			return;
		}
		DocumentNode document = parser.getDocumentNode();
		WriterVisitor visitor = new WriterVisitor();
		document.accept(visitor);
	}

	/**
	 * Implementacija {@link INodeVisitor} koja pokušava rekreirati
	 * izgled skripte prije njegovog parsiranja
	 * @author Luka Dragutin
	 *
	 */
	public static class WriterVisitor implements INodeVisitor {

		/**Rezultat obrade*/
		StringBuilder text = new StringBuilder();

		@Override
		public void visitTextNode(TextNode node) {
			text.append(node.getText().replaceAll("\\\\", "\\\\\\\\"));
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			text.append("{$ FOR " + node.getVariable().asText() + " " + node.getStartExpression().asText() + " "
					+ node.getEndExpression().asText());
			if (node.getStepExpression() != null) {
				text.append(" " + node.getStepExpression().asText());
			}
			text.append("$}");
			WriterVisitor.visitChildrenNodes(node, this);
			text.append("{$END$}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			text.append("{$ = ");
			Element[] elements = node.getElements();
			for (var e : elements) {
				if (e == null) {
					break;
				}
				text.append(e.asText() + " ");
			}
			text.append("$}");
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			visitChildrenNodes(node, this);
			System.out.println(text.toString());
		}

		/**
		 * Posjećuje svu djecu predanog čvora {@code node} i delegira nad njima
		 * odgovarajuću metodu {@link INodeVisitor} ovisno o njihovom tipu
		 * @param node Roditeljski čvor
		 * @param visitor Posjetitelj
		 * @throws RuntimeException Ako dijete nije podranog tipa
		 */
		public static void visitChildrenNodes(Node node, INodeVisitor visitor) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				Node child = node.getChild(i);
				if (child instanceof ForLoopNode) {
					visitor.visitForLoopNode((ForLoopNode) child);
				} else if (child instanceof EchoNode) {
					visitor.visitEchoNode((EchoNode) child);
				} else if (child instanceof TextNode) {
					visitor.visitTextNode((TextNode) child);
				} else if(child instanceof TimeNode) {
					visitor.visitTimeNode((TimeNode) child);
				}
				else {
					throw new RuntimeException();
				}
			}
		}

		@Override
		public void visitTimeNode(TimeNode node) {
			// TODO Auto-generated method stub
			
		}

	}

}
