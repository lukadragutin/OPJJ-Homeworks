package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.*;

/**
 * Pomocni program za testiranje funkcionalnosti sucelja {@link ElementsGetter}
 * i njegovih implementacija u kolekcijama
 * @author Luka
 *
 */
public class ElementsGetterDemo {

	public static void main(String[] args) {
		
		Collection col1 = new ArrayIndexedCollection();
		Collection col2 = new ArrayIndexedCollection();
		col1.add("Ivo");
		col1.add("Ana");
		col1.add("Jasna");
		col2.add("Jasmina");
		col2.add("Štefanija");
		col2.add("Karmela");
		
		ElementsGetter getter1 = col1.createElementsGetter();
		ElementsGetter getter2 = col1.createElementsGetter();
		ElementsGetter getter3 = col2.createElementsGetter();
		
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
		
		Collection col = new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		
		ElementsGetter getter = col.createElementsGetter();
		
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		
		//col.clear();
		
		System.out.println("Jedan element: " + getter.getNextElement());
		
		getter2.processRemaining(System.out::println);
	}

}
