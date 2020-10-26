package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

/**
 * Razred koji filtira podatke iz baze podataka na
 * osnovu više izraza koje treba zadovoljiti.
 * Uvjeti su objekti tipa {@link ConditionalExpression} kroz
 * čiju provjeru prolaze predani argumenti koji moraju zadovoljiti
 * svaki od njih da bi bili zadovoljavajući. 
 * @author Luka Dragutin
 *
 */
public class QueryFilter implements IFilter {

	/**Lista uvjeta za filitriranje*/
	private List<ConditionalExpression> filters;
	
	/**Stvara novi primjerak objekta {@link QueryFilter}
	 * @param filters Lista uvjeta koji se trebaju zadovoljiti
	 * @throws NullPointerException ako je lista <code>null</code>
	 */
	public QueryFilter(List<ConditionalExpression> filters) {
		this.filters = Objects.requireNonNull(filters);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws NullPointerException ako je predani argument <code>null</code>
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for(var c : filters) {
			if(!c.getComparisonOperator().satisfied(
						c.getFieldGetter().get(record),
						c.getStringLiteral())) {
				return false;
			}
		}
		return true;
	}
}
