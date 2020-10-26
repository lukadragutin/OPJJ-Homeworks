package hr.fer.zemris.java.gui.calc.model;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;
import javax.swing.JLabel;


/**
 * Razred koji modelira gumbe kalkulatora
 * sa binarnim operacijama 
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class CalcOperationButton extends JButton {

	/** Operacija koju gumb predstavlja */
	private DoubleBinaryOperator operator;

	
	public CalcOperationButton(String operator, DoubleBinaryOperator operation, CalcModel calc, JLabel screen,
			SecretSlot slot) {
		super(operator);
		this.operator = Objects.requireNonNull(operation);
		super.setFont(super.getFont().deriveFont(30f));

		super.addActionListener(e -> {
			var op = calc.getPendingBinaryOperation();

			if (op == null) {
				calc.setActiveOperand(calc.getValue());
			} else {

				if (op.equals(DefaultOperations.POW) || op.equals(DefaultOperations.ROOT)) {
					calc.setValue(op.applyAsDouble(slot.x, calc.getValue()));
					calc.setPendingBinaryOperation(slot.old);
					if (slot.old == null) {
						calc.setActiveOperand(calc.getValue());
					} else {
						op = calc.getPendingBinaryOperation();
						calc.setActiveOperand(op.applyAsDouble(calc.getActiveOperand(), calc.getValue()));

					}
				} else {
					if (!calc.isActiveOperandSet()) {
						throw new CalculatorInputException("Operand must be set first!");
					}
					calc.setActiveOperand(op.applyAsDouble(calc.getActiveOperand(), calc.getValue()));
				}
			}
			calc.setPendingBinaryOperation(this.operator);
			calc.clear();
			double result = calc.getActiveOperand();
			screen.setText(
					String.valueOf(result).endsWith(".0") ? String.valueOf((int) result) : String.valueOf(result));
		});
	}

}
