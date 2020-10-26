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


/**
 * Nasljeđuje {@link GeometricalObjectEditor} i omogućava izmjenu vrijednosti
 * objekta {@link Circle}
 * @author LukaD
 *
 */
public class CircleEditor extends GeometricalObjectEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Krug kojeg uređuje
	 */
	protected Circle circle;
	
	/**
	 * Komponenta za unos nove vrijednosti x koordinate središta
	 */
	protected JTextField centerX;
	
	/**
	 * Komponenta za unos nove vrijednosti y koordinate središta
	 */
	protected JTextField centerY;
	
	/**
	 * Komponenta za unos nove vrijednosti radijusa
	 */
	protected JTextField radius;
	
	/**
	 * Komponenta za prikaz izabrane vrijednosti boje
	 */
	protected JLabel colorLabel;

	public CircleEditor(Circle circle) {
		this.circle = circle;
		init();
	}

	/**
	 * Inicijalizira izgled prozora za uređivanje objekta
	 */
	protected void init() {
		setLayout(new GridLayout(0, 2));

		JLabel labelX = new JLabel("center(X): ");
		add(labelX);
		centerX = new JTextField(circle.getCenter().x);
		centerX.setText(Integer.toString(circle.getCenter().x));
		add(centerX);

		JLabel labelY = new JLabel("center(Y): ");
		add(labelY);
		centerY = new JTextField();
		centerY.setText(Integer.toString(circle.getCenter().y));
		add(centerY);

		JLabel radiusLabel = new JLabel("radius: ");
		add(radiusLabel);
		radius = new JTextField();
		radius.setText(Integer.toString(circle.getRadius()));
		add(radius);

		colorLabel = new JLabel("#" + Util.getHexColor(circle.getColor()));
		colorChooseAction = getChooser(colorLabel, this);
		
		JButton colorChooser = new JButton(colorChooseAction);
		add(colorChooser);
		add(colorLabel);
	}

	@Override
	public void checkEditing() {
		int radius;
		try {
			Integer.parseInt(centerX.getText());
			Integer.parseInt(centerY.getText());
			radius = Integer.parseInt(this.radius.getText());
		} catch (Exception e) {
			throw new RuntimeException("Invalid values.");
		}

		if (radius <= 0)
			throw new RuntimeException("Radius cannot be negative or 0.");
	}

	@Override
	public void acceptEditing() {
		int x = Integer.parseInt(centerX.getText());
		int y =	Integer.parseInt(centerY.getText());
		int radius = Integer.parseInt(this.radius.getText());
		Color color = Util.getColorHex(colorLabel.getText().substring(1));
		
		circle.setCenter(new Point(x, y));
		circle.setRadius(radius);
		circle.setColor(color);
	}

	/**
	 * Akcija biranja nove boje
	 */
	protected Action colorChooseAction;

	
	/**
	 * Stvara i vraća novu akciju {@link #colorChooseAction}
	 * @param label Komponenta za pohranu nove vrijednost boje
	 * @param parent Roditeljska komponenta
	 * @return novi {@link #colorChooseAction}
	 */
	protected Action getChooser(JLabel label, Container parent) {
		return new AbstractAction("Choose color") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(parent, "Choose a color I dare you", circle.getColor());
				if (c != null) {
					label.setText("#" + Util.getHexColor(c));
					parent.repaint();
				}
			}
		};
	}

}
