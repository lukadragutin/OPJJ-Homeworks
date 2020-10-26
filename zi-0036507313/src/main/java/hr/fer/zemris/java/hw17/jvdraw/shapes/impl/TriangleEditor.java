package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Color;
import java.awt.Container;
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

public class TriangleEditor extends GeometricalObjectEditor{

	
	
	private Triangle triangle;
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
	
	private JTextField x3tf;
	private JTextField y3tf;
	
	private JLabel bgColorLabel;
	
	
	
	
	public TriangleEditor(Triangle triangle) {
		this.triangle = triangle;
		init();
	}

	private void init() {
		setLayout(new GridLayout(0, 2));

		JLabel x1 = new JLabel("X1: ");
		add(x1);
		X1tf = new JTextField();
		X1tf.setText(Integer.toString(triangle.getX().x));
		add(X1tf);

		JLabel y1 = new JLabel("Y1: ");
		add(y1);
		y1tf = new JTextField();
		y1tf.setText(Integer.toString(triangle.getX().y));
		add(y1tf);

		JLabel x2 = new JLabel("X2: ");
		add(x2);
		X2tf = new JTextField();
		X2tf.setText(Integer.toString(triangle.getY().x));
		add(X2tf);

		JLabel y2 = new JLabel("Y2: ");
		add(y2);
		y2tf = new JTextField();
		y2tf.setText(Integer.toString(triangle.getY().y));
		add(y2tf);
		
		JLabel x3 = new JLabel("X3: ");
		add(x3);
		x3tf = new JTextField();
		x3tf.setText(Integer.toString(triangle.getZ().x));
		add(x3tf);

		JLabel y3 = new JLabel("Y3: ");
		add(y3);
		y3tf = new JTextField();
		y3tf.setText(Integer.toString(triangle.getZ().y));
		add(y3tf);

		colorLabel = new JLabel("#" + Util.getHexColor(triangle.getColor()));

		bgColorLabel = new JLabel("#" + Util.getHexColor(triangle.getBgColor()));

		colorChooseAction1 = getChooser(colorLabel, this);
		colorChooseAction2 = getChooser(bgColorLabel, this);
		
		JButton colorChooser = new JButton(colorChooseAction1);
		add(colorChooser);
		add(colorLabel);
		
		JButton colorChooser2 = new JButton(colorChooseAction2);
		add(colorChooser2);
		add(bgColorLabel);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(X1tf.getText());
			Integer.parseInt(X2tf.getText());
			Integer.parseInt(y1tf.getText());
			Integer.parseInt(y2tf.getText());
			Integer.parseInt(x3tf.getText());
			Integer.parseInt(y3tf.getText());
		} catch (Exception e) {
			throw new RuntimeException("Invalid coordinate values.");
		}
	}

	@Override
	public void acceptEditing() {
		Point x = new Point(Integer.parseInt(X1tf.getText()), Integer.parseInt(y1tf.getText()));
		Point y = new Point(Integer.parseInt(X2tf.getText()), Integer.parseInt(y2tf.getText()));
		Point z = new Point(Integer.parseInt(x3tf.getText()), Integer.parseInt(y3tf.getText()));
		Color color = Util.getColorHex(colorLabel.getText().substring(1));
		Color bgColor = Util.getColorHex(bgColorLabel.getText().substring(1));

		
		triangle.setX(x);
		triangle.setY(y);
		triangle.setZ(z);
		triangle.setColor(color);
		triangle.setBgColor(bgColor);
		}
	protected Action colorChooseAction1;
	protected Action colorChooseAction2;

	
	protected Action getChooser(JLabel label, Container parent) {
		return new AbstractAction("Choose color") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(parent, "Choose a color I dare you", triangle.getColor());
				if (c != null) {
					label.setText("#" + Util.getHexColor(c));
					parent.repaint();
				}
			}
		};
	}

	
	
}
