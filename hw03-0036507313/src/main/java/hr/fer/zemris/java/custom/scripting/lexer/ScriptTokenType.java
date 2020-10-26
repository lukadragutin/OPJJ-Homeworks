package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Tipovi razreda {@link ScriptToken}
 * @author Luka Dragutin
 *
 */
public enum ScriptTokenType {

	/**
	 *Token sa vrijednosti double
	 */
	DOUBLE,
	
	/**
	 * Token sa vrijednosti integer
	 */
	INTEGER,
	
	/**
	 * Token varijable koji pripada tag izrazu
	 */
	VARIABLE,
	
	/**
	 *Token koji je operator u tag izrazu
	 */
	OPERATOR,
	
	/**
	 * Token koji oznacava kraj datoteke
	 */
	EOF,
	
	/**
	 * Token koji ima vrijednost String
	 */
	STRING,
	
	/**
	 * Token koji cuva izraz za neku funkciju
	 */
	FUNCTION,
	
	/**
	 * Token koji oznacava kraj tag izraza
	 */
	ENDTAG,
	
	/**
	 * Token koji oznacava pocetak tag izraza
	 */
	STARTTAG
}
