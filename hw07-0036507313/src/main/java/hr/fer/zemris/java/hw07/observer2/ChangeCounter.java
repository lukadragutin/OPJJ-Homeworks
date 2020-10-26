package hr.fer.zemris.java.hw07.observer2;

public class ChangeCounter implements IntegerStorageObserver {

	private int changes;
	
	@Override
	public void valueChanged(IntegerStorageChange ischange) {
		System.out.println("Number of value changes since tracking: " + ++changes);
	}
	
}
