package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

public class IntegerStorageChange {

	private IntegerStorage istorage;
	private int oldValue;
	private int newValue;
	
	public IntegerStorageChange(IntegerStorage istorage, int oldValue, int newValue) {
		if(oldValue == newValue) {
			throw new IllegalArgumentException("New value can't be the same as the old value!");
		}
		this.istorage = Objects.requireNonNull(istorage);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public IntegerStorage getIstorage() {
		return istorage;
	}

	public int getOldValue() {
		return oldValue;
	}

	public int getNewValue() {
		return newValue;
	}
	
}
