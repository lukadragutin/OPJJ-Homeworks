package hr.fer.zemris.java.hw07.observer1;

public class DoubleValue implements IntegerStorageObserver {

	private int limit;
	private int counter;
	
	public DoubleValue(int limit) {
		if(limit < 1) {
			throw new IllegalArgumentException("Limit of changes must be number larger than 0!");
		}
		this.limit = limit;
	}
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.println("Double value: " + istorage.getValue() * 2);
		if(++counter == limit) {
			istorage.removeObserver(this);
		}
	}
	
	
}
