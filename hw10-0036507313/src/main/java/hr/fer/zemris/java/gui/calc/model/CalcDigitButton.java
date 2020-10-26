package hr.fer.zemris.java.gui.calc.model;

import java.util.Objects;

import javax.swing.JButton;


/**
 * Razred koji modelira gumbe kalkulatora
 * sa znamenkama
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class CalcDigitButton extends JButton {

	/**Znamenka koju gumb predstavlja */
	private int digit;
	
	/** Kalkulator */
	private CalcModel calc;
	
	/**
	 * Stvara novi primjerak razreda {@link CalcDigitButton}
	 * @param digit Znamenka koju predstavlja gumb
	 * @param calc Model kalkulatora
	 * @throws IllegalArgumentException Ako je kao znamenka predan
	 * broj koji nije jednoznamenkast
	 */
	public CalcDigitButton(int digit, CalcModel calc) {
		super(String.valueOf(digit));
		this.calc = Objects.requireNonNull(calc);
		super.setFont(super.getFont().deriveFont(30f));
		
		if(digit < 0 || digit > 9) {
			throw new IllegalArgumentException("Value of button must be a single digit");
		}
		this.digit = digit;
		super.addActionListener(l -> this.calc.insertDigit(this.digit));
	}
}
