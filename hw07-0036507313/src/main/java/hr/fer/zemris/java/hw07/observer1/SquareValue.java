package hr.fer.zemris.java.hw07.observer1;


/**
 * Kod svake promjene ispise kvadrat nove vrijednosti
 * @author Luka
 *
 */
public class SquareValue implements IntegerStorageObserver {
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		int value = istorage.getValue();
		System.out.println("Provided new value: " + value + ", square is " + value*value);
	}

}
