package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Color;
import java.awt.Point;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;

import hr.fer.zemris.java.hw17.jvdraw.util.Util;


/**
 * Nasljeđuje {@link CircleEditor} i omogućava izmjenu objekta tipa {@link FilledCircle}
 * @author LukaD
 *
 */
public class FilledCircleEditor extends CircleEditor {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Primjerak koji se uređuje
	 */
	private FilledCircle filledCircle;

	/**
	 * Komponenta za prikaz vrijednosti nove boje ispunjenja
	 */
	private JLabel bgColor;
	
	/**
	 * Akcija biranja boje pozadine kruga
	 */
	private Action bgColorChooser;
	
	public FilledCircleEditor(FilledCircle filledCircle) {
		super(new Circle(filledCircle.getCenter(), filledCircle.getRadius(), filledCircle.getColor()));
		this.filledCircle = filledCircle;
		init2();
	}
	
	
	/**
	 * Dodaje na prozor kojeg je napravio {@link CircleEditor} mogućnost
	 * mijenjanja boje pozadine
	 */
	private void init2() {

		bgColor= new JLabel("#" + Util.getHexColor(filledCircle.getBgColor()));
		bgColorChooser = getChooser(bgColor, this);	
		JButton bgColorButton = new JButton(bgColorChooser);
		add(bgColorButton);
		add(bgColor);
	}

	@Override
	public void acceptEditing() {
		int x = Integer.parseInt(centerX.getText());
		int y =	Integer.parseInt(centerY.getText());
		int radius = Integer.parseInt(this.radius.getText());
		Color color = Util.getColorHex(colorLabel.getText().substring(1));
		Color bgCol = Util.getColorHex(bgColor.getText().substring(1));
		
		filledCircle.setCenter(new Point(x, y));
		filledCircle.setRadius(radius);
		filledCircle.setColor(color);
		filledCircle.setBgColor(bgCol);
	}
}
