package hr.fer.zemris.java.hw07.demo3;

import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;

public class ValueWrapperDemo {

	public static void main(String[] args) {

		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());     // v1 now stores Integer(0); v2 still stores null.
		
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue());     // v3 now stores Double(13); v4 still stores Integer(1).
		
		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
		v5.add(v6.getValue());     // v5 now stores Integer(13); v6 still stores Integer(1).
		
		ValueWrapper v7 = new ValueWrapper("Ankica");
		ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));
		//v7.add(v8.getValue());     // throws RuntimeExcepti
		
		v3.numCompare(v5.getValue());
		v1.numCompare(v2.getValue());
		v6.numCompare(v4.getValue());
		v1.numCompare(v5.getValue());
	}

}
