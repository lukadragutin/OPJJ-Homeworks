package searching.algorithms;


/**
 * Uredeni par nekog stanja i 'cijene' 
 * koje to stanje kosta, tj. koliko je trebalo koraka(vremena)
 * da se dosegne to stanje od pocetnog stanja.
 *
 * @author Luka Dragutin
 * 
 * @param <S> Tip stanja
 */
public class Transition<S> {

	/**
	 * Stanje
	 */
	private S state;
	
	/**
	 * cijena
	 */
	private double cost;
	
	public Transition(S state, double cost) {
		this.state = state;
		this.cost = cost;
	}

	public S getState() {
		return state;
	}

	public double getCost() {
		return cost;
	}
	
}
