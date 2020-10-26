package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.Tester;

/**
 * Program za testiranje sucelja {@link Tester}
 * @author Luka
 *
 */
public class TesterDemo {

	static class EvenIntegerTester implements Tester {
		public boolean test(Object obj) {
			if(!(obj instanceof Integer)) return false;
			Integer i = (Integer)obj;
			return i % 2 == 0;
		}
	}
	
	public static void main(String[] args) {
		Tester t = new EvenIntegerTester();
		System.out.println(t.test("Ivo"));
		System.out.println(t.test(22));
		System.out.println(t.test(3));
		
		Collection col1 = new LinkedListIndexedCollection();
		Collection col2 = new ArrayIndexedCollection();
		
		col1.add(2);
		col1.add(3);
		col1.add(4);
		col1.add(5);
		col1.add(6);
		col2.add(12);
		col2.addAllSatisfying(col1, t);
		col2.forEach(System.out::println);
	}

}
