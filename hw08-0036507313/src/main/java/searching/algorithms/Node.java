package searching.algorithms;

/**
 * Cvor koji cuva stanje slagalice, 
 * referencu na roditeljski cvor (korak nesposredno prije) i
 * 'cijenu' dolaska do ovog stanja.
 * @author Luka Dragutin
 * @param <S> Tip stanja cvora
 */
public class Node<S> {

	/**
	 * Referenca na roditeljski cvor
	 */
	private Node<S> parent;
	
	/**
	 * Stanje cvora
	 */
	private S state;
	
	/**
	 * 'Cijena' dolaska do ovog stanja
	 */
	private double cost;
	
	public Node(Node<S> parent, S state, double cost) {
		this.parent = parent;
		this.state = state;
		this.cost = cost;
	}
	
	public S getState() {
		return state;
	}
	
	public double getCost() {
		return cost;
	}
	
	public Node<S> getParent() {
		return parent;
	}
	
	
}
