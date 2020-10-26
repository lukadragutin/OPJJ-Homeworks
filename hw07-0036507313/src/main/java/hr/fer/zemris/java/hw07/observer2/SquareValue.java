package hr.fer.zemris.java.hw07.observer2;

public class SquareValue implements IntegerStorageObserver {
	
	@Override
	public void valueChanged(IntegerStorageChange ischange) {
		int value = ischange.getNewValue();
		System.out.println("Provided new value: " + value + ", square is " + value*value);
	}

}
