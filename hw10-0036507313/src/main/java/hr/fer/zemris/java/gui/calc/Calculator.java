package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.gui.calc.model.CalcDigitButton;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcOperationButton;
import hr.fer.zemris.java.gui.calc.model.CalcUnaryOperatorButton;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.DefaultOperations;
import hr.fer.zemris.java.gui.calc.model.PowRootButton;
import hr.fer.zemris.java.gui.calc.model.SecretSlot;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;


/**
 * Kalkulator.
 * Jednostavan kalkulator koji nudi operacije zbrajanja, oduzimanja,
 * mnozenja, dijeljenja i jednostavne trigonometrijske operacije.
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class Calculator extends JFrame {

	/** Model kalkulatora */
	private CalcModel calc = new CalcModelImpl();
	
	/**Lista gumba koji imaju dvije funkcionabilnosti*/
	private LinkedList<CalcUnaryOperatorButton> invertedButtons;

	/**Komponenta koja predstavlja ekran kalkulatora */
	private JLabel screen;
	
	/**Stog kalkulatora*/
	private Stack<Double> stack = new Stack<>();
	
	/**Varijabla za pomoc pri racunanju korijena i potencija*/
	private SecretSlot x = new SecretSlot(1, null);

	public Calculator() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI();
		setSize(500, 500);
	}
	//Inicijalizacija grafickih komponenti
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(2));

		this.screen = new JLabel("", SwingConstants.RIGHT);
		screen.setFont(screen.getFont().deriveFont(30f));
		screen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		screen.setBackground(Color.YELLOW);
		screen.setOpaque(true);
		
		var calcScreen = new CalcValueListenerImpl(screen);
		calc.addCalcValueListener(calcScreen);
		cp.add(screen, new RCPosition(1, 1));

		JButton equals = new JButton("=");
		equals.setFont(equals.getFont().deriveFont(30f));
		equals.addActionListener((l) -> {
			var op = calc.getPendingBinaryOperation();
			if(op.equals(DefaultOperations.ROOT) || op.equals(DefaultOperations.POW)) {
					calc.setValue(op.applyAsDouble(x.x, calc.getValue()));
					calc.setPendingBinaryOperation(x.old);
					op = x.old;
			}
			if (calc.isActiveOperandSet()) {
				calc.setValue(op.applyAsDouble(calc.getActiveOperand(), calc.getValue()));
			} else {
				calc.setValue(calc.getValue());
			}
			double result = calc.getValue();
			calc.clearAll();
			screen.setText(
					String.valueOf(result).endsWith(".0") ? String.valueOf((int) result) : String.valueOf(result));
		});
		cp.add(equals, new RCPosition(1, 6));

		JButton invert = new JButton("1/x");
		invert.addActionListener((l) -> {
			calc.setValue(1 / calc.getValue());
		});
		cp.add(invert, "2,1");

		setInvertedPart(cp);
		setDigits(cp);
		setBinaryOperators(cp);

		PowRootButton pwb = new PowRootButton(x, calc);
		cp.add(pwb, "5,1");

		JButton swapSign = new JButton("+/-");
		swapSign.setFont(swapSign.getFont().deriveFont(25f));
		swapSign.addActionListener((l) -> calc.swapSign());
		cp.add(swapSign, "5,4");

		JButton point = new JButton(".");
		point.setFont(point.getFont().deriveFont(30f));
		point.addActionListener((l) -> calc.insertDecimalPoint());
		cp.add(point, "5,5");

		JButton clr = new JButton("clr");
		clr.addActionListener((l) -> calc.clear());
		cp.add(clr, new RCPosition(1, 7));

		JButton reset = new JButton("reset");
		reset.addActionListener((l) -> calc.clearAll());
		cp.add(reset, "2,7");

		JButton push = new JButton("push");
		push.addActionListener((l) -> {
			stack.push(calc.getValue());
		});
		cp.add(push, "3,7");

		JButton pop = new JButton("pop");
		pop.addActionListener((l) -> {
			if (stack.isEmpty()) {
				screen.setText("Stack is empty!");
			} else {
				calc.setValue(stack.pop());
			}
		});
		cp.add(pop, "4,7");

		JCheckBox inverted = new JCheckBox("inv");
		inverted.addActionListener((l) -> {
			invertedButtons.forEach((e) -> e.setInverted(inverted.isSelected()));
			pwb.setInverted(inverted.isSelected());
		});
		cp.add(inverted, "5,7");
	}
	//Inicijalizacija tipki za binarne operacije
	private void setBinaryOperators(Container cp) {
		cp.add(new CalcOperationButton("/", DefaultOperations.DIVIDE, calc, screen, x), "2,6");
		cp.add(new CalcOperationButton("*", DefaultOperations.MULTIPLY, calc, screen, x), "3,6");
		cp.add(new CalcOperationButton("-", DefaultOperations.SUB, calc, screen, x), "4,6");
		cp.add(new CalcOperationButton("+", DefaultOperations.ADD, calc, screen, x), "5,6");
	}
	//Inicijalizacija tipki sa znamenkama
	private void setDigits(Container cp) {
		for (int i = 2; i < 5; i++) {
			int konst = -2 + (4 - i) * 3;
			for (int j = 3; j < 6; j++) {
				cp.add(new CalcDigitButton(j + konst, calc), new RCPosition(i, j));
			}
		}
		cp.add(new CalcDigitButton(0, calc), "5,3");
	}
	
	//Inicijalizacija tipki za dvije funkcionabilnosti
	private void setInvertedPart(Container cp) {

		invertedButtons = new LinkedList<>();

		CalcUnaryOperatorButton sin = new CalcUnaryOperatorButton("sin", "arcsin", Math::sin, Math::asin, calc);
		cp.add(sin, new RCPosition(2, 2));
		invertedButtons.add(sin);

		CalcUnaryOperatorButton cos = new CalcUnaryOperatorButton("cos", "arccos", Math::cos, Math::acos, calc);
		cp.add(cos, "3,2");
		invertedButtons.add(cos);

		CalcUnaryOperatorButton tan = new CalcUnaryOperatorButton("tan", "arctan", Math::tan, Math::atan, calc);
		cp.add(tan, "4,2");
		invertedButtons.add(tan);

		CalcUnaryOperatorButton ctg = new CalcUnaryOperatorButton("ctg", "arcctg", (d) -> 1 / Math.tan(d),
				(d) -> Math.PI / 2 - Math.atan(d), calc);
		cp.add(ctg, "5,2");
		invertedButtons.add(ctg);

		CalcUnaryOperatorButton log = new CalcUnaryOperatorButton("log", "10^x", Math::log10, (d) -> Math.pow(10, d),
				calc);
		cp.add(log, "3,1");
		invertedButtons.add(log);

		CalcUnaryOperatorButton ln = new CalcUnaryOperatorButton("ln", "e^x", Math::log, Math::exp, calc);
		cp.add(ln, "4,1");
		invertedButtons.add(ln);
	}

	/**
	 * Razred promatrac koji obavijestava ekran kalkulatora 
	 * ako dode do promjena vrijednosti u kalkulatoru da ih moze
	 * ispisati na ekran
	 * @author Luka Dragutin
	 *
	 */
	private static class CalcValueListenerImpl implements CalcValueListener {

		/** Ekran kalkulatora */
		private JLabel screen;

		public CalcValueListenerImpl(JLabel screen) {
			this.screen = screen;
		}

		@Override
		public void valueChanged(CalcModel model) {
			screen.setText(model.toString());
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}
}
