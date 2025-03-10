package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;

/** Test implementacije kolekcija */
public class CollectionTest {

	public static void main(String[] args) {
		ArrayIndexedCollection col = new ArrayIndexedCollection(2);
		col.add(Integer.valueOf(20));
		col.add("New York");
		col.add("San Francisco");  // here the internal array is reallocated to 4
		System.out.println(col.contains("New York")); // writes: true
		col.remove(1); // removes "New York"; shifts "San Francisco" to position 1
		System.out.println(col.get(1)); // writes: "San Francisco"
		System.out.println(col.size()); // writes: 2
		col.add("Los Angeles");
		LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(col);
		// This is local class representing a Processor which writes objects to System.out
		class P extends Processor {
			public void process(Object o) {
				System.out.println(o);
				}
		};
		System.out.println("col elements:");
		col.forEach(new P());
		System.out.println("col elements again:");
		System.out.println(Arrays.toString(col.toArray()));
		System.out.println("col2 elements:");
		col2.forEach(new P());
		System.out.println("col2 elements again:");
		System.out.println(Arrays.toString(col2.toArray()));
		System.out.println(col.contains(col2.get(1)));// true
		System.out.println(col2.contains(col.get(1))); // true
		col.remove(Integer.valueOf(20)); // removes 20 from collection (at position 0)
	}

}
