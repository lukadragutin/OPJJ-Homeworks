package hr.fer.zemris.java.hw07.observer1;

/**
 * Razred koji broji broj promjena nad zadanim objektom
 * @author Luka
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	/**
	 * broj promjena
	 */
	private int changes;
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.println("Number of value changes since tracking: " + ++changes);
	}
	
}
