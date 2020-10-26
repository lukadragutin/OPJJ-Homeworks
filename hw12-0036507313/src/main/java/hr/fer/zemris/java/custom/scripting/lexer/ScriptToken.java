package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

/**
 * Razred koji modelira token za {@link ScriptingLexer}
 * Token je grupacija bitnih znakova
 * @author Luka Dragutin
 *
 */
public class ScriptToken {

	/**
	 * Tip tokena
	 */
	private ScriptTokenType type;
	
	/**
	 * Vrijednost tokena
	 */
	private Object value;
	
	/**
	 * Stvara novi primjerak rezreda {@link ScriptToken} sa tipom type i vrijednosti value 
	 * @param type tip tokena
	 * @param value vrijednost tokena
	 * @throws NullPointerException ako je type null
	 */
	public ScriptToken(ScriptTokenType type, Object value) {
		this.type = Objects.requireNonNull(type);
		this.value = value;
	}

	/**
	 * @return Koji tip tokena iz enum ScriptTypeToken
	 */
	public ScriptTokenType getType() {
		return type;
	}

	/**
	 * @return Vrijednost pohranjena u tokenu
	 */
	public Object getValue() {
		return value;
	}
}

