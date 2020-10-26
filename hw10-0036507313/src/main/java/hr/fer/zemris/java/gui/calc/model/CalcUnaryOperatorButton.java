package hr.fer.zemris.java.gui.calc.model;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;


/**
 * Razred koji modelira gumbe kalkulatora
 * sa unarnim operacijama (jednim argumentom), 
 * koje ujedno imaju i dvije funkcije ovisno o
 * vrijednosti inverted
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class CalcUnaryOperatorButton extends JButton {
	
	/**Naziv operacije kad nije invertiran*/
	private String textNotInverted;
	
	/**Naziv operacije kad je invertiran*/
	private String textInverted;
	
	public CalcUnaryOperatorButton(String textNotInverted, String textInverted, DoubleUnaryOperator operatorNotInverted, DoubleUnaryOperator operatorInverted, CalcModel calc) {
		super(Objects.requireNonNull(textNotInverted));
		
		this.textNotInverted = Objects.requireNonNull(textNotInverted);
		this.textInverted = Objects.requireNonNull(textInverted);
		
		super.addActionListener((e) -> {
			if(super.getText().equals(textNotInverted)) {
				calc.setValue(operatorNotInverted.applyAsDouble(calc.getValue()));
			}
			else {
				calc.setValue(operatorInverted.applyAsDouble(calc.getValue()));
			}
		});
	}
	
	public void setInverted(boolean inverted) {
		if(inverted) {
			super.setText(textInverted);
		}
		else {
			super.setText(textNotInverted);
		}
	}
}
