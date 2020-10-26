package hr.fer.zemris.java.gui.calc.model;

import java.util.function.DoubleBinaryOperator;

/**
 * Razred koji nudi implementacije nekih
 * binarnih operacija
 * @author Luka Dragutin
 *
 */
public class DefaultOperations {

	/** Operacija zbrajanja */
	public final static DoubleBinaryOperator ADD = (x, y) -> x + y;
	
	/**Operacija oduzimanja */
	public final static DoubleBinaryOperator SUB = (x, y) -> x - y;
	
	/**Operacija mnozenja*/
	public final static DoubleBinaryOperator MULTIPLY = (x, y) -> x * y;
	
	/**Operacija dijeljenja*/
	public final static DoubleBinaryOperator DIVIDE = (x, y) -> x / y;
	
	/**Operacija potenciranja */
	public final static DoubleBinaryOperator POW = (x, y) -> Math.pow(x, y);
	
	/**Operacija korijenovanja*/
	public final static DoubleBinaryOperator ROOT = (x, y) -> Math.pow(x, 1/y);
}
