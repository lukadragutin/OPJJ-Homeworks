package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.util.Util;

/**
 * Primjerak editora {@link GeometricalObjectEditor} nad razredom {@link Line}
 * @author LukaD
 *
 */
public class LineEditor extends GeometricalObjectEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Primjerak linije koja se uređuje
	 */
	private Line line;
	
	/**
	 * Komponenta za prikaz izabrane boje
	 */
	private JLabel colorLabel;
	
	/**
	 * Komponenta za prikaz odabrane x koordinate prve tocke linije
	 */
	private JTextField X1tf;
	
	/**
	 *  Komponenta za prikaz odabrane y koordinate prve tocke linije
	 */
	private JTextField X2tf;
	
	/**
	 *  Komponenta za prikaz odabrane x koordinate druge tocke linije
	 */
	private JTextField y1tf;
	
	/**
	 *  Komponenta za prikaz odabrane y koordinate druge tocke linije
	 */
	private JTextField y2tf;
	

	public LineEditor(Line line) {
		this.line = line;
		setSize(200, 250);
		initDialog();
	}

	/**
	 * Inicijalizira izgled prozora za unošenje novih vrijednosti
	 */
	private void initDialog() {

		setLayout(new GridLayout(0, 2));

		JLabel x1 = new JLabel("X1: ");
		add(x1);
		X1tf = new JTextField();
		X1tf.setText(Integer.toString(line.getX().x));
		add(X1tf);

		JLabel y1 = new JLabel("Y1: ");
		add(y1);
		y1tf = new JTextField();
		y1tf.setText(Integer.toString(line.getX().y));
		add(y1tf);

		JLabel x2 = new JLabel("X2: ");
		add(x2);
		X2tf = new JTextField();
		X2tf.setText(Integer.toString(line.getY().x));
		add(X2tf);

		JLabel y2 = new JLabel("Y2: ");
		add(y2);
		y2tf = new JTextField();
		y2tf.setText(Integer.toString(line.getY().y));
		add(y2tf);

		JButton colorChooser = new JButton(colorChooseAction);
		add(colorChooser);
		colorLabel = new JLabel("#" + Util.getHexColor(line.getColor()));
		add(colorLabel);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(X1tf.getText());
			Integer.parseInt(X2tf.getText());
			Integer.parseInt(y1tf.getText());
			Integer.parseInt(y2tf.getText());
		} catch (Exception e) {
			throw new RuntimeException("Invalid coordinate values.");
		}
	}

	@Override
	public void acceptEditing() {
		Point x = new Point(Integer.parseInt(X1tf.getText()), Integer.parseInt(X2tf.getText()));
		Point y = new Point(Integer.parseInt(y1tf.getText()), Integer.parseInt(y2tf.getText()));
		Color color = Util.getColorHex(colorLabel.getText().substring(1));
		
		line.setX(x);
		line.setY(y);
		line.setColor(color);
	}

	public Action colorChooseAction = new AbstractAction("Choose color") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Color c = JColorChooser.showDialog(LineEditor.this, "Choose a color I dare you", line.getColor());
			if (c != null) {
				colorLabel.setText(Util.getHexColor(c));
			}
		}
	};

}
