package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

class SmartScriptParserTest {

	@Test
	void ConstructorNull() {
		assertThrows(NullPointerException.class, () -> new SmartScriptParser(null));
	}
	
	@Test
	void EmptyString() {
		assertDoesNotThrow(() -> new SmartScriptParser(""));
	}
	
	@Test
	void WrongForTag() {
		assertThrows(SmartScriptParserException.class , () -> new SmartScriptParser("{$ fro i23 3 4 9$})"));
	}
	
	@Test
	void WrongVariable() {
		assertThrows(SmartScriptParserException.class , () -> new SmartScriptParser("{$ for 2i3 3 4 9$})"));
	}
	
	@Test
	void TooManyArguments() {
		assertThrows(SmartScriptParserException.class , () -> new SmartScriptParser("{$ FOR i23 3 4 9 \"23\" $})"));
	}

	@Test
	void TooLittleArguments() {
		assertThrows(SmartScriptParserException.class , () -> new SmartScriptParser("{$ fro i23 3 $})"));
	}
	
	@Test
	void WrongEscape() {
		assertThrows(SmartScriptParserException.class , () -> new SmartScriptParser("This is how not to use backslash\\3"));
	}
	
	@Test
	void WrongEcho() {
		assertThrows(SmartScriptParserException.class , () -> new SmartScriptParser("{$ i = i23 3 $})"));
	}
	
	@Test
	void test() {
		SmartScriptParser p = new SmartScriptParser("This is sample text.\r\n" + 
				"{$ FOR i 1 10 1 $}\r\n" + 
				"  This is {$= i $}-th time this message is generated.\r\n" + 
				"{$END$}\r\n" + 
				"{$FOR i 0 10 2 $}\r\n" + 
				"  sin({$=i$}^2) = {$= i i * @sin  \"0.000\" @decfmt $}\r\n" + 
				"{$END$}");
		DocumentNode document = p.getDocumentNode();
		assertEquals(4, document.numberOfChildren());
		assertEquals("This is sample text.\r\n", ((TextNode) document.getChild(0)).getText());
		assertEquals("i", (((ForLoopNode) document.getChild(1)).getVariable().asText()));
	}
}
