package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;

/**
 * Omotac za vrijednosti u stogu {@link ObjectMultistack}
 * @author Luka Dragutin
 *
 */
public class ValueWrapper {

	
	interface OperationDouble extends Operation<Double> {}

	interface OperationInt extends Operation<Integer> {}

	
	/**
	 * Vrijednost koju cuva
	 */
	private Object value;
	
	/**
	 * Mapa operacija nad double brojevima
	 */
	private static HashMap<Operator, OperationDouble> operationsDouble;
	
	/**
	 * Mapa operacija nad int brojevima
	 */
	private static HashMap<Operator, OperationInt> operationsInt;	

	static {
		operationsDouble = new HashMap<>();
		operationsDouble.put(Operator.ADD, (x, y) -> x + y);
		operationsDouble.put(Operator.SUB, (x, y) -> x - y);
		operationsDouble.put(Operator.MUL, (x, y) -> x * y);
		operationsDouble.put(Operator.DIV, (x, y) -> x / y);
		operationsInt = new HashMap<>();
		operationsInt.put(Operator.DIV, (x, y) -> x / y);
		operationsInt.put(Operator.ADD, (x, y) -> x + y);
		operationsInt.put(Operator.MUL, (x, y) -> x * y);
		operationsInt.put(Operator.SUB, (x, y) -> x - y);
		
	}

	public ValueWrapper(Object value) {
		super();
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public static HashMap<Operator, OperationInt> getOperationsInt() {
		return operationsInt;
	}
	
	public static HashMap<Operator, OperationDouble> getOperationsDouble() {
		return operationsDouble;
	}

	/**
	 * Operacija zbrajanja
	 * @param incValue Pribrojnik
	 */
	public void add(Object incValue) {
		StackOperator add = new StackOperator();
		value = add.operate(this.value, incValue, Operator.ADD);
		}

	/**
	 * Operacija oduzimanja
	 * @param decValue Vrijednost koju oduzimamo
	 */
	public void subtract(Object decValue) {
		StackOperator add = new StackOperator();
		value = add.operate(this.value, decValue, Operator.SUB);
	}

	/**
	 * Operacija mnozenja
	 * @param mulValue mnozitelj
	 */
	public void multiply(Object mulValue) {
		StackOperator add = new StackOperator();
		value = add.operate(this.value, mulValue, Operator.MUL);
	}

	/**
	 * Operacij dijeljenja
	 * @param divValue Dijelitelj
	 */
	public void divide(Object divValue) {
		StackOperator add = new StackOperator();
		value = add.operate(this.value, divValue, Operator.DIV);
	}

	/**
	 * Usporeduje vrijednost objekta sa predanom
	 * Vraca negativnan broj ako je manji od predanog argumenta, pozitivan ako
	 * je veci, a 0 ako su jednaki
	 * @param withValue Argument s kojim se usporeduje
	 */
	public int numCompare(Object withValue) {
		if(value == null) {
			if(withValue == null) {
				return 0;
			}
			else return -1;
		}
		if(withValue == null) {
			return 1;
		}
		StackOperator so = new StackOperator();
		var result = so.operate(value, withValue, Operator.SUB);
		if(result instanceof Double) {
			return (int)((double) result);
		}
		else return (int) result;
	}


}
