package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.lexer.LexerState;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptToken;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptTokenType;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptingLexer;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.hw03.prob1.LexerException;
import hr.fer.zemris.java.custom.scripting.elems.*;

/**
 * Razred koji parsira ulazne Stringove. 
 * @author Luka Dragutin
 *
 */
public class SmartScriptParser {
	
	/**
	 * Lexer za dohvacanje tokena
	 */
	private ScriptingLexer lexer;
	
	/**
	 * Stog za spremanje obradenih tokena
	 */
	private ObjectStack stack;
	
	/**
	 * Stvara novi parser koji odmah po pokretanju konstruktora parsira ulazni String
	 * @param docBody Ulazni String
	 * @throws NullPointerException ako je argument docbody null
	 * {@link SmartScriptParserException} ako dode do pogreske pri parsiranju (sintakticki krivi ulazni String)
	 */
	public SmartScriptParser(String docBody) {
		lexer = new ScriptingLexer(Objects.requireNonNull(docBody));
		stack = new ObjectStack();
		try{
			parse();
		} catch (LexerException ex) {
			throw new SmartScriptParserException();
		}
	}
	
	/**
	 * Dohvaca parsirani ulazni niz u obliku primjerka razreda {@link DocumentNode}
	 * @return {@link DocumentNode} sa obradenim ulaznim Stringom
	 * @throws SmartScriptParserException() ako dode do pogreske pri dohvacanju
	 */
	public DocumentNode getDocumentNode() {
		try {
			DocumentNode node = (DocumentNode) stack.peek();
			return node;
		} catch (EmptyStackException ex) {
			throw new SmartScriptParserException("No document node");
		}
	}
	
	/**
	 * pomocna metoda za parsiranje ulaznog niza
	 * @throws SmartScriptParserException ako dode do pogreske pri parsiranju
	 */
	private void parse() {
		DocumentNode node = new DocumentNode();
		stack.push(node);
		ScriptToken token = lexer.nextToken();
		if(token.getType() == ScriptTokenType.EOF) {
			return;
		}
		do {
			if(lexer.getState() == LexerState.TEXT) {
				parseText(token);
			}
			else {
				parseTag(token);
			}
			token = lexer.nextToken();
		} while(token.getType() != ScriptTokenType.EOF);
	}
	
	/**
	 * Pomocna metoda za parsiranje String podniza ulaznog Stringa
	 * @param token sa substringom iz ulaznog Stringa
	 */
	private void parseText(ScriptToken token) {
		if(token.getType() != ScriptTokenType.STRING) {
			throw new SmartScriptParserException();
		}
		ElementString text = new ElementString((String)token.getValue());
		TextNode node = new TextNode(text.asText());
		Node parent = (Node) stack.peek();
		parent.addChildNode(node);
	}
	/**
	 * Pomocna metoda za obradu tag izraza
	 * @param token koji se obraduje
	 * @throws SmartScriptParserException ako token nije valjani dio tag izraza
	 */
	private void parseTag(ScriptToken token) {
		if(token.getType() == ScriptTokenType.STARTTAG) {
			token = lexer.nextToken();
		}
		if(token.getValue().toString().equalsIgnoreCase("for")) {
			parseFor(token);
		}
		else if(token.getValue().equals('=')) {
			parseEquals(token);
		}
		else if(token.getValue().toString().equals("END")) {
			endTag(token);
		}
		else {
			throw new SmartScriptParserException();
		}
	}
	
	/**
	 * Pomocna metoda za obradu tokena koji oznacava kraj tag izraza "for"
	 * @param token koji oznacava kraj tag izraza
	 * @throws SmartScriptParserException ako nije tocna oznaka za kraj tag izraza,
	 * ili se vise puta zatvorio tag nego otvorio
	 */
	private void endTag(ScriptToken token) {
		try{
			stack.pop();
		} catch(EmptyStackException ex) {
			throw new SmartScriptParserException("Invalid number of end tags");
		}
		token = lexer.nextToken();
		if(token.getType() != ScriptTokenType.ENDTAG) {
			throw new SmartScriptParserException("Wrond end tag format");
		}
	}

	/**
	 * Pomocna metoda za obradu tag echo izraza
	 * @param token Koji oznacava pocetak tog izraza
	 * @throws SmartScriptParserException ako je neki od argumenata krivo zadan
	 */
	private void parseEquals(ScriptToken token) {
		Element[] elements = new Element[16];
		int i = 0;
		token = lexer.nextToken();
		while(token.getType() != ScriptTokenType.ENDTAG) {
			elements[i] = getElement(token);
			i++;
			token = lexer.nextToken();
		}
		EchoNode node = new EchoNode(elements);
		Node parent = (Node) stack.peek();
		parent.addChildNode(node);
	}
	/**
	 * Pretvara vrijednost tokena u odgovarajuci primjerak razreda {@link Element} i vraca ga
	 * @param token Cija se vrijednost pretvara u Element
	 * @return {@link Element} koji odgovara vrijednosti ulaznog tokena
	 * @throws SmartScriptParserException ako tokena nema ispravnu vrijednost.
	 */
	private Element getElement(ScriptToken token) {
		if(token.getType() == ScriptTokenType.VARIABLE) {
			return new ElementVariable(token.getValue().toString());
		}
		else if(token.getType() == ScriptTokenType.FUNCTION) {
			return new ElementFunction(token.getValue().toString());
		}
		else if(token.getType() == ScriptTokenType.STRING) {
			return new ElementString(token.getValue().toString());
		}
		else if(token.getType() == ScriptTokenType.OPERATOR) {
			return new ElementOperator(token.getValue().toString());
		}
		else if(token.getType() == ScriptTokenType.INTEGER) {
			return new ElementConstantInteger((int) token.getValue());
		}
		else if(token.getType() == ScriptTokenType.DOUBLE) {
			return new ElementConstantDouble((double) token.getValue());
		}
		else {
			throw new SmartScriptParserException();
		}
	}

	/**
	 * Pomocna metoda za obradu tag izraza "for"
	 * @param token Oznacava pocetak tag izraza for
	 * @throws SmartScriptParserException ako su krivi argumenti u tag izrazu
	 */
	private void parseFor(ScriptToken token) {
		token = lexer.nextToken();
		if(token.getType() != ScriptTokenType.VARIABLE) {
			throw new SmartScriptParserException();
		}
		ElementVariable variable = (ElementVariable) getElement(token);
		Element[] arguments = new Element[3];
		int i;
		for(i = 0; i < 3; i++) {
			token = lexer.nextToken();
			if(token.getType() == ScriptTokenType.ENDTAG) {
				if(i < 2) {
					throw new SmartScriptParserException("Invalid number of arguments");
				}
				else {
					break;
				}
			}
			arguments[i] = parseNumber(token);
		}
		if(i == 3 && ((token = lexer.nextToken()).getType() != ScriptTokenType.ENDTAG)) {
			throw new SmartScriptParserException("Too many arguments");
		}
		ForLoopNode node = new ForLoopNode(variable, arguments[0], arguments[1], arguments[2]);
		Node parent = (Node) stack.peek();
		parent.addChildNode(node);
		stack.push(node);
	}

	/**
	 * Pomocna metoda za parsiranje argumenata tag izraza for
	 * u odgovarajuce primjerke razreda {@link Element}
	 * @param token argument tag izraza
	 * @return Element od vrijednosti ulaznog tokena
	 */
	private Element parseNumber(ScriptToken token) {
		if(token.getType() == ScriptTokenType.STRING) {
			return getElement(token);
		}
		else if(token.getType() == ScriptTokenType.DOUBLE) {
			return getElement(token);
		}
		else if(token.getType() == ScriptTokenType.INTEGER) {
			return getElement(token);
		}
		else {
			throw new SmartScriptParserException("Wrong argument type");
		}
	}
}
