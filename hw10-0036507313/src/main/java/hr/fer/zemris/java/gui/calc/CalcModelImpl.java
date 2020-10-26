package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Implementacija sucelja {@link CalcModel}
 * @see {@link CalcModel}
 * @author Luka Dragutin
 *
 */
public class CalcModelImpl implements CalcModel {

	/** Odreduje je li trenutna vrijednost negativna*/
	private boolean negative;
	
	/** Odreduje moze li se trenutna vrijednost uredivati*/
	private boolean editable;
	
	/** String zapis trenutne vrijednosti*/
	private String textValue;
	
	/** Trenutna vrijednost */
	private double value;
	
	/** Operacija koja ce se iduca izvrsiti*/
	private DoubleBinaryOperator pendingOperation;
	
	/** Trenutni operand*/
	private double activeOperand;
	
	/**	Zastavica koja odreduje je li postavljen operand */
	private boolean activeOperandSet;
	
	/** Promatraci */
	private List<CalcValueListener> listeners;


	/**
	 * Konstruktor razreda {@link CalcModelImpl}, nema argumenata
	 */
	public CalcModelImpl() {
		clear();
		activeOperandSet = false;
	}

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		if (listeners == null) {
			listeners = new ArrayList<>();
		}
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		if (listeners != null) {
			listeners.remove(l);
		}
	}

	@Override
	public double getValue() {
		return negative ? -value : value;
	}

	@Override
	public void setValue(double value) {
		if (value < 0) {
			negative = true;
		} else {
			negative = false;
		}

		this.value = Math.abs(value);
		if (Double.isNaN(value)) {
			textValue = "NaN";
		} else if (Double.isInfinite(value)) {
			textValue = "Infinity";
		} else {
			this.textValue = String.valueOf(this.value);
		}
		editable = false;
		if (listeners != null) {
			listeners.forEach(e -> e.valueChanged(this));
		}
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		editable = true;
		negative = false;
		value = 0;
		textValue = null;
		if (listeners != null) {
			listeners.forEach(e -> e.valueChanged(this));
		}
	}

	@Override
	public void clearAll() {
		clear();
		activeOperand = 0;
		pendingOperation = null;
		activeOperandSet = false;
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (!editable) {
			throw new CalculatorInputException();
		}
		negative = !negative;
		if (listeners != null) {
			listeners.forEach(e -> e.valueChanged(this));
		}
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (!editable) {
			throw new CalculatorInputException();
		}
		if (textValue == null) {
			throw new CalculatorInputException("No value set!");
		}
		if (textValue.contains(".")) {
			throw new CalculatorInputException("Value cannot have more than one decimal point!");
		}
		textValue += ".";
		if (listeners != null) {
			listeners.forEach(e -> e.valueChanged(this));
		}
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if (!editable) {
			throw new CalculatorInputException();
		}

		if (textValue == null || textValue.equals("0")) {
			textValue = "";
		}

		if (textValue.length() > 307) {
			throw new CalculatorInputException("Out of reach!");
		}

		textValue += String.valueOf(digit);
		try {
			value = Double.parseDouble(textValue);
		} catch (NumberFormatException e) {
			throw new CalculatorInputException(e.getMessage());
		}
		if (listeners != null) {
			listeners.forEach(e -> e.valueChanged(this));
		}
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperandSet;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (!activeOperandSet) {
			throw new IllegalStateException("Operand not set!");
		}
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		activeOperandSet = true;
		if (listeners != null) {
			listeners.forEach(e -> e.valueChanged(this));
		}
	}

	@Override
	public void clearActiveOperand() {
		activeOperandSet = false;
		if (listeners != null) {
			listeners.forEach(e -> e.valueChanged(this));
		}
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingOperation = op;
		if (listeners != null) {
			listeners.forEach(e -> e.valueChanged(this));
		}
	}

	@Override
	public String toString() {
		return (negative ? "-" : "") + (textValue == null ? "0" : textValue);
	}

}
