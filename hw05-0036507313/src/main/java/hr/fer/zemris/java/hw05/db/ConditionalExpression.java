package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Razred koji modelira izraz za upravljanje
 * bazama podataka. Omogućuje filtriranje prema jednoj
 * od clanskih varijabli elementa baze podataka korištenjem
 * jednog primjerka {@link ComparisonOperators} i Stringa 
 * s kojim ga uspoređujemo.
 * @author Luka Dragutin
 *
 */
public class ConditionalExpression {

	/**
	 * Služi za dohvatanje varijable na osnovu koje filtiriramo podatke
	 */
	private IFieldValueGetter fieldGetter;
	
	/**
	 * Operator za uspoređivanje varijable kojeg traženi podaci moraju zadovoljiti
	 */
	private IComparisonOperator comparisonOperator;
	
	/**
	 * String s kojim uspoređujemo varijable elemenata baze podataka
	 */
	private String stringLiteral;
	
	
	public ConditionalExpression(IFieldValueGetter fieldGetter, IComparisonOperator comparisonOperator,
			String stringLiteral) {
		super();
		this.fieldGetter = Objects.requireNonNull(fieldGetter);
		this.comparisonOperator = Objects.requireNonNull(comparisonOperator);
		this.stringLiteral = Objects.requireNonNull(stringLiteral);
	}


	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}


	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}


	public String getStringLiteral() {
		return stringLiteral;
	}	
}
