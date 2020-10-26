package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ScriptingLexerTest {

	@Test
	void ConstructorNull() {
		assertThrows(NullPointerException.class, () -> new ScriptingLexer(null));
	}
	@Test
	void NextTokenText() {
		ScriptingLexer l = new ScriptingLexer("I am a new lexer!");
		ScriptToken token = l.nextToken();
		assertEquals("I am a new lexer!", token.getValue().toString());
		assertEquals(ScriptTokenType.STRING, token.getType());
	}
	
	@Test
	void TextTokenEscape() {
		ScriptingLexer l = new ScriptingLexer("I have backslashs \\{$");
		ScriptToken token = l.nextToken();
		assertEquals("I have backslashs {$", token.getValue().toString());
	}
	
	@Test
	void GetToken() {
		ScriptingLexer l = new ScriptingLexer("{$ for me 2 3 4$}");
		ScriptToken token = l.nextToken();
		assertEquals(token, l.getToken());
	}
	
	@Test
	void NextTokenTag() {
		ScriptingLexer l = new ScriptingLexer("{$ for me 2.2.2  4$}");
		ScriptToken token = l.nextToken();
		assertEquals(ScriptTokenType.STARTTAG, token.getType());
		token = l.nextToken();
		assertEquals(ScriptTokenType.VARIABLE, token.getType());
		assertEquals("for", token.getValue().toString());
		token = l.nextToken();
		token = l.nextToken();
		assertEquals(ScriptTokenType.DOUBLE, token.getType());
		assertEquals(2.2, (double) token.getValue());
		assertThrows(LexerException.class, () -> l.nextToken());
	}
	
	@Test
	void NextTokenEndTag() {
		ScriptingLexer l = new ScriptingLexer("{$$}$}");
		ScriptToken token = l.nextToken();
		token = l.nextToken();
		assertEquals(ScriptTokenType.ENDTAG, token.getType());
		token = l.nextToken();
		assertEquals(ScriptTokenType.STRING, token.getType());
	}

}
