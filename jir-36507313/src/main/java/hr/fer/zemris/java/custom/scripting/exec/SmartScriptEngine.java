package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.demo.TreeWriter;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.nodes.TimeNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Čita i pokrece primjerke pisane u obliku SmartScript.
 * 
 * @author Luka Dragutin
 *
 */
public class SmartScriptEngine {

	/** Vrhovni čvor skripte */
	private DocumentNode documentNode;

	/** Kontekst komunikacije sa klijentom */
	private RequestContext requestContext;

	/** Stog za pohranu medurezultata i parametara */
	private ObjectMultistack multistack = new ObjectMultistack();

	/** 'Šetač' po čvorovima */
	private INodeVisitor visitor = new INodeVisitor() {

		/**
		 * Ispisuje sadržaj {@link TextNode} {@code node} na izlaz u
		 * {@link RequestContext}
		 */
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
			}
		}

		/**
		 * Iterira po djeci {@link ForLoopNode} čvora ovisno o njegovim parametrima
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			var endValue = node.getEndExpression().asText();
			multistack.push(variable, new ValueWrapper(node.getStartExpression().asText()));
			while (multistack.peek(variable).numCompare(endValue) <= 0) {
				TreeWriter.WriterVisitor.visitChildrenNodes(node, this);
				multistack.peek(variable).add(node.getStepExpression().asText());
//				try {
//					requestContext.write("\r\n");
//				} catch (IOException e) {
//				}
			}
			multistack.pop(variable);
		}

		/**
		 * Izvršava operacije ili funkcije zadane kao elementi ovog čvora
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			var elements = node.getElements();
			Stack<ValueWrapper> stack = new Stack<>();
			for (Element e : elements) {
				if (e instanceof ElementConstantDouble || e instanceof ElementConstantInteger) {
					stack.push(new ValueWrapper(e.asText()));
				} else if (e instanceof ElementVariable) {
					stack.push(multistack.peek(e.asText()));
				} else if (e instanceof ElementOperator) {
					stack.push(calculateOperator(stack.pop(), stack.pop(), (ElementOperator) e));
				} else if (e instanceof ElementFunction) {
					Util.calculateFunction(e.asText(), stack, requestContext);
				} else if (e instanceof ElementString) {
					stack.push(new ValueWrapper(e.asText()));
				}
			}
			try {
				for (int i = 0; i < stack.size(); i++) {
					requestContext.write((stack.get(i).getValue()).toString().getBytes());
				}
			} catch (IOException e1) {
				throw new RuntimeException(e1.getMessage());
			}
		}

		/**
		 * Pomocna metoda za operiranje dvaju vrijednosti sa {@link ElementOperator}
		 * {@code e}
		 * 
		 * @param x Prvi operand
		 * @param y Drugi operand
		 * @param e Operator
		 * @return Rezultat operacije
		 */
		private ValueWrapper calculateOperator(ValueWrapper x, ValueWrapper y, ElementOperator e) {
			Operator op = Util.getOperator(e.asText());
			StackOperator so = new StackOperator();
			return new ValueWrapper(so.operate(x.getValue(), y.getValue(), op));
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			TreeWriter.WriterVisitor.visitChildrenNodes(node, this);
		}

		@Override
		public void visitTimeNode(TimeNode node) {
			String formatString = node.getText();
			DateTimeFormatter dtf = null;
			if (formatString != null) {
				try {
					dtf = DateTimeFormatter.ofPattern(formatString);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			} else {
				dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");	
			}
			var time = LocalDateTime.now();
			
			try {
				requestContext.write(time.format(dtf));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = Objects.requireNonNull(documentNode);
		this.requestContext = Objects.requireNonNull(requestContext);
	}

	/**
	 * Pokreće obradu skripte
	 */
	public void execute() {
		documentNode.accept(visitor);
		try {
			requestContext.write("\r\n");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
