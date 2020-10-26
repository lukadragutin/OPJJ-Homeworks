package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.Token;
import hr.fer.zemris.java.hw05.db.lexer.TokenType;

/**
 * Razred koji parsira naredbe unesene preko komandne linije.
 * 
 * @author Luka Dragutin
 *
 */
public class QueryParser {

	/** Lexer kojim se dohvaćaju riječi i operatori */
	private QueryLexer lexer;

	/** Lista svih postavljenih uvjeta */
	private List<ConditionalExpression> query;

	/**
	 * Stvara novi primjerak iz zapisa naredbe i odmah parsira predani zapis.
	 * 
	 * @throws NullPointerException ako je String zapis <code>null</code>
	 * @param query Zapis sa naredbama/uvjetima za upravljanje bazama podataka
	 */
	public QueryParser(String query) {
		this.query = new ArrayList<>();
		lexer = new QueryLexer(query);
		parse();
	}

	/**
	 * Pomoćna metoda za parsiranje zapisa.
	 */
	private void parse() {
		ConditionalExpression expression = getExpression();
		while (expression != null) {
			query.add(expression);
			expression = getExpression();
		}
	}

	/**
	 * Dohvaca jedan uvjet iz zapisa ako ga ima, ako ne vraća <code>null</code>
	 * 
	 * @throws IllegalArgumentException ako je zapis krivo strukturiran
	 */
	private ConditionalExpression getExpression() {
		Token<String> t1 = lexer.nextToken();
		if (t1.getType().equals(TokenType.END)) {
			return null;
		}
		if (t1.getType().equals(TokenType.AND)) {
			if (query.isEmpty()) {
				throw new IllegalArgumentException("Invalid query! AND has to be followed by an expression");
			} else {
				t1 = lexer.nextToken();
			}
		}
		if (!t1.getType().equals(TokenType.FIELDNAME)) {
			throw new IllegalArgumentException("Invalid query! First argument must be a field name!");
		}
		var field = fieldGetter(t1.getValue());
		t1 = lexer.nextToken();
		if (!t1.getType().equals(TokenType.OPERATOR)) {
			throw new IllegalArgumentException("Invalid query! Field name must be followed by an operator!");
		}
		var operator = getComparisonOperator(t1.getValue());
		t1 = lexer.nextToken();
		if (!t1.getType().equals(TokenType.STRING)) {
			throw new IllegalArgumentException("Invalid query! Operator must be followed by a string literal!");
		}
		String string = t1.getValue();
		return new ConditionalExpression(field, operator, string);
	}

	/**
	 * Pomoćna metoda za dohvat primjerka objekta {@link ComparisonOperators} na
	 * osnovu String zapisa operatora.
	 * 
	 * @param value String zapis operatora
	 * @return Objekt koji predstavlja predani operator
	 * @throws IllegalArgumentException ako je netočan operator.
	 */
	private IComparisonOperator getComparisonOperator(String value) {
		switch (value) {
		case "=":
			return ComparisonOperators.EQUALS;
		case "<":
			return ComparisonOperators.LESS;
		case "<=":
			return ComparisonOperators.LESS_OR_EQUALS;
		case ">":
			return ComparisonOperators.GREATER;
		case ">=":
			return ComparisonOperators.GREATER_OR_EQUALS;
		case "!=":
			return ComparisonOperators.NOT_EQUALS;
		case "LIKE":
			return ComparisonOperators.LIKE;
		default:
			throw new IllegalArgumentException("Wrong operator!");
		}
	}

	/**
	 * Pomoćna metoda za dohvat primjerka {@link FieldValueGetters} koji odgovara
	 * predanom string zapisu varijable.
	 * 
	 * @param value String zapis tražene varijable
	 * @return Primjerak razreda {@link FieldValueGetters} koji odgovara argumentu
	 */
	private IFieldValueGetter fieldGetter(String value) {
		switch (value) {
		case "jmbag":
			return FieldValueGetters.JMBAG;
		case "firstName":
			return FieldValueGetters.FIRST_NAME;
		case "lastName":
			return FieldValueGetters.LAST_NAME;
		default:
			throw new IllegalArgumentException("Wrong field name entry!");
		}
	}

	/**
	 * Provjerava sadržava li naredba samo jedan uvjet koji traži studenta sa
	 * određenim jmbag-om i vraća <code>true</code> ako je istina,
	 * <code>false</code> inače.
	 */
	public boolean isDirectQuery() {
		return query.size() == 1 && query.get(0).getFieldGetter().equals(FieldValueGetters.JMBAG)
				&& query.get(0).getComparisonOperator().equals(ComparisonOperators.EQUALS);
	}

	/**
	 * Vraća jmbag koji je tražen u izravnoj naredbi.
	 * 
	 * @throws IllegalStateException ako naredba nije bila izravna.
	 */
	public String getQuerriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("Not a direct query!");
		} else {
			return query.get(0).getStringLiteral();
		}
	}

	/**
	 * Vraća popis uvjeta parsiranih iz naredbe
	 */
	public List<ConditionalExpression> getQuery() {
		return query;
	}

}
