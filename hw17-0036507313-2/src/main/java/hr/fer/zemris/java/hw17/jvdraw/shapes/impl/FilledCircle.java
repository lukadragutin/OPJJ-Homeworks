package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.util.Util;


/**
 * Razred koji nasljeđuje {@link Circle} i dodaje mu samo
 * još ispunu boje {@link #bgColor}
 * @author LukaD
 *
 */
public class FilledCircle extends Circle {

	/**
	 * Boja unutrašnjosti kruga
	 */
	private Color bgColor;

	
	public FilledCircle(Point center, int radius, Color fgColor, Color bgColor) {
		super(center, radius, fgColor);
		this.bgColor = Objects.requireNonNull(bgColor);
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}

	/**
	 *  getter za {@link #bgColor}
	 */
	public Color getBgColor() {
		return bgColor;
	}

	@Override
	public String toString() {
		return String.format("Filled circle(%s,%s), %s, #%s", getCenter().x, getCenter().y,
				getRadius(), Util.getHexColor(bgColor));
	}

	/**
	 *  Setter za {@link #bgColor}
	 */
	public void setBgColor(Color bgColor) {
		if (!this.bgColor.equals(bgColor)) {
			fire();
		}
		this.bgColor = bgColor;
	}

}
