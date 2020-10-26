package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Jednostavan program za testiranje {@link SmartScriptParser} razreda koji
 * preko komandne linije prima poveznicu na datoteku koja se treba parsirati
 * @author Luka
 *
 */
public class SmartScriptTester {

	public static void main(String[] args) throws IOException {
		
		String docBody = new String(Files.readAllBytes(Paths.get(args[0])),StandardCharsets.UTF_8);
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch(Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody); // should write something like original
											      // content of docBod
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();// now document and document2 should be structurally identical tree
		

	}
	/**
	 * Pretvara parsirani dokument natrag u stanje blisko originalnom
	 * @param document Node koji je rezultat parsiranja datoteke
	 * @return String teksta iz parsirane datoteke
	 */
	private static String createOriginalDocumentBody(DocumentNode document) {
		return processChildren(document);
	}
	/**
	 * Pretvara svu djecu roditeljskog nodea u String
	 * @param parent Roditelj ciju djecu pretvaramo u Stringove
	 * @return String kao rezultat spajanja djece u jedinstven String
	 */
	private static String processChildren(Node parent) {
		String text = "";
		for(int i = 0; i < parent.numberOfChildren(); i++) {
			Node child = parent.getChild(i);
			if(child.getClass() == ForLoopNode.class) {
				text += processFor((ForLoopNode) child);
			}
			else if(child.getClass() == EchoNode.class) {
				text += processEcho((EchoNode) child);
			}
			else if(child.getClass() == TextNode.class) {
				text += processText((TextNode) child);
			}
		}
		return text;
	}
	
	/**
	 * Pretvara TextNode u String i mijenja ne parseabilne dijelove u parseabilne
	 * @param child TextNode iz kojeg izvlacimo String
	 * @return String vrijednost TextNodea child
	 */
	private static String processText(TextNode child) {
		return child.getText().replaceAll("\\\\", "\\\\\\\\");
	}
	
	/**
	 * Pretvara EchoNode u originalni String zapis iz datoteke
	 * @param child Parsirani zapis uredenog izraza iz datoteke
	 * @return String zapis prije parsiranja
	 */
	private static String processEcho(EchoNode child) {
		String text = "{$ = ";
		Element[] elements = child.getElements();
		for(var e:elements) {
			if(e == null) {
				break;
			}
			text += e.asText() + " ";
		}
		text += "$}";
		return text;
	}

	/**
	 * Pretvara ForLoopNode u originalni String zapis
	 * iz datoteke prije parsiranja
	 * @param node Koji se pretvara u originalni String zapis
	 * @return String zapis prije parsiranja
	 */
	private static String processFor(ForLoopNode node) {
		String text = "{$ FOR " + node.getVariable().asText() + " " + node.getStartExpression().asText() + " " + node.getEndExpression().asText();
		if(node.getStepExpression() != null) {
			text+=" " + node.getStepExpression().asText(); 
		}
		text += "$}";
		text += processChildren(node);
		text += "{$END$}";
		return text;
	} 

}
