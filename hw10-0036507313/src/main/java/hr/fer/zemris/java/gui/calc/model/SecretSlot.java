package hr.fer.zemris.java.gui.calc.model;

import java.util.function.DoubleBinaryOperator;

/**
 * Pomocni razred koji modelira
 * objekt za pomoc pri racunanju operacija
 * potenciranja i korijenovanja u kalkulatoru
 * @author Luka Dragutin
 *
 */
public class SecretSlot {

	/**
	 * Vrijednost koju potenciramo/korijenujemo
	 */
	public double x;
	
	/**
	 * Operacija koja je bila zakazana prije 
	 * operacije potenciranja/korijenovanja
	 */
	public DoubleBinaryOperator old;
	
	public SecretSlot(double x, DoubleBinaryOperator old) {
		this.x = x;
		this.old = old;
	}
	
}
