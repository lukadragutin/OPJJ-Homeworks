package hr.fer.zemris.java.hw07.observer2;

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
	public void valueChanged(IntegerStorageChange ischange) {
		System.out.println("Double value: " + ischange.getNewValue() * 2);
		if(++counter == limit) {
			ischange.getIstorage().removeObserver(this);
		}
	}
	
	
}
