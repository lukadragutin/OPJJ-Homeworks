package hr.fer.zemris.java.gui.calc.model;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class PowRootButton extends JButton {

	private static String pow = "x^n";
	private static String root = "x^(1/n)";
	
	public PowRootButton(SecretSlot x, CalcModel calc) {
		super(pow);
		
		super.addActionListener((l) -> {
			x.old = calc.getPendingBinaryOperation();
			x.x = calc.getValue();
			if(super.getText().equals(pow)) {
				calc.setPendingBinaryOperation(DefaultOperations.POW);
			}
			else {
				calc.setPendingBinaryOperation(DefaultOperations.ROOT);
			}
			calc.clear();
		});
	}
	
	public void setInverted(boolean inverted) {
		super.setText(inverted ? root : pow);
	}
}
