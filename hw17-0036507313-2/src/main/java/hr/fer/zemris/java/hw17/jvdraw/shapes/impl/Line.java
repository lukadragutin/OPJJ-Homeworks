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
 * Podrazred {@link GeometricalObject} razreda koji implementira geometrijski
 * entitet linije.
 * @author LukaD
 *
 */
public class Line extends GeometricalObject {

	/**
	 * Točka polaznica linije
	 */
	private Point x;
	
	/**
	 * Toška odrednica linije
	 */
	private Point y;
	
	/**
	 * Boja linije
	 */
	private Color color;

	
	/**
	 * Promatrači {@link GeometricalObjectListener} nad ovim objektom
	 */
	private List<GeometricalObjectListener> listeners = new ArrayList<>();

	public Line(Point x, Point y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	/**
	 *  Getter za {@link #X}
	 */
	public Point getX() {
		return x;
	}

	/**
	 *  Fetter za {@link #Y}
	 */
	public Point getY() {
		return y;
	}

	/**
	 * Getter za {@link #color}
	 */
	public Color getColor() {
		return color;
	}

	/**
	 *  Setter za {@link #X}
	 */
	public void setX(Point x) {
		if(!this.x.equals(x)) {
			fire();
		}
		this.x = x;
	}

	

	/**
	 *  Setter za {@link #Y}
	 */
	public void setY(Point y) {
		if(!this.y.equals(y)) {
			fire();
		}
		this.y = y;
	}

	/**
	 *  Setter za {@link #color}
	 */
	public void setColor(Color color) {
		if(!this.color.equals(color)) {
			fire();
		}
		this.color = color;
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}

	@Override
	public void addGeometricalListener(GeometricalObjectListener l) {
		listeners.add(l);
	}

	@Override
	public void removeGeometricalListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}
	
	
	/**
	 * Javlja promjene svojim promatračima
	 */
	private void fire() {
		listeners.forEach(l -> l.geometricalObjectChanged(this));
	}
	
	@Override
	public String toString() {
		return String.format("Line(%s,%s)-(%s,%s)", x.x, x.y, y.x, y.y);
	}

}
