package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectListener;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectVisitor;

/**
 * Podrazred razreda {@link GeometricalObject} koji implementira krug
 * 
 * @author LukaD
 *
 */
public class Circle extends GeometricalObject {

	/**
	 * Središte kruga
	 */
	protected Point center;

	/** Radijus kruga */
	protected int radius;

	/** Boja kruga */
	protected Color color;

	/**
	 * Promatrači nad ovim krugom
	 */
	protected List<GeometricalObjectListener> listeners = new ArrayList<>();

	public Circle(Point center, int radius, Color color) {
		this.center = center;
		this.radius = radius;
		this.color = color;
	}

	/**
	 * Getter za {@link #center}
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * Getter za {@link #radius}
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Getter za {@link #color}
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setter za {@link #center}
	 */
	public void setCenter(Point center) {
		if (!this.center.equals(center)) {
			fire();
		}
		this.center = center;
	}

	/**
	 * Setter za {@link #radius}
	 */
	public void setRadius(int radius) {
		if (this.radius != radius) {
			fire();
		}
		this.radius = radius;
	}

	/**
	 *  Setter za {@link #color}
	 */
	public void setColor(Color color) {
		if (!this.color.equals(color)) {
			fire();
		}
		this.color = color;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}

	@Override
	public void addGeometricalListener(GeometricalObjectListener l) {
		listeners.add(l);
	}

	@Override
	public void removeGeometricalListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}

	@Override
	public String toString() {
		return String.format("Circle(%s,%s), %s", center.x, center.y, radius);
	}

	/**
	 * Dojavljuje svojim promatračima o nastalim promjenama parametara
	 */
	protected void fire() {
		listeners.forEach(l -> l.geometricalObjectChanged(this));
	}
}
