package hr.fer.zemris.java.hw07.observer1;

import java.util.ArrayList;
import java.util.List;

public class IntegerStorage {

	private int value;
	private List<IntegerStorageObserver> observers;
	
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}
	
	public void addObserver(IntegerStorageObserver observer) {
		if(observers == null) {
			observers = new ArrayList<>();
		}
		if(!observers.contains(observer)) {
			observers = new ArrayList<>(observers);
			observers.add(observer);
		}
	}
	
	public void removeObserver(IntegerStorageObserver observer) {
		if(observers != null) {
			observers = new ArrayList<>(observers);
			observers.remove(observer);
			
		}
	}
	
	public void clearObservers() {
		observers = new ArrayList<>();
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		if(this.value != value) {
			this.value = value;
			if(observers != null) {
				observers.forEach(e -> e.valueChanged(this));
			}
		}
	}
}
