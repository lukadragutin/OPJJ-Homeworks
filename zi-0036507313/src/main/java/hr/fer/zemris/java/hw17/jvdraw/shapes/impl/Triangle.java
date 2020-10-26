package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectListener;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectVisitor;

public class Triangle extends GeometricalObject {

	private Point x;
	private Point y;
	private Point z;
	private Color color;
	private Color bgColor;
	private List<GeometricalObjectListener> listeners;
	
	
	
	public Triangle(Point x, Point y, Point z, Color color, Color bgColor) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.bgColor = bgColor;
		listeners = new ArrayList<>();
	}
	
	

	/**
	 * @return the x
	 */
	public Point getX() {
		return x;
	}



	/**
	 * @param x the x to set
	 */
	public void setX(Point x) {
		this.x = x;
		fire();
	}



	/**
	 * @return the y
	 */
	public Point getY() {
		return y;
	}



	/**
	 * @param y the y to set
	 */
	public void setY(Point y) {
		this.y = y;
		fire();
	}



	/**
	 * @return the z
	 */
	public Point getZ() {
		return z;
	}



	/**
	 * @param z the z to set
	 */
	public void setZ(Point z) {
		this.z = z;
		fire();
	}



	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}



	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
		fire();
	}



	/**
	 * @return the bgColor
	 */
	public Color getBgColor() {
		return bgColor;
	}



	/**
	 * @param bgColor the bgColor to set
	 */
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
		fire();
	}



	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new TriangleEditor(this);
	}

	@Override
	public void addGeometricalListener(GeometricalObjectListener l) {
		listeners.add(l);
	}

	@Override
	public void removeGeometricalListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}

	private void fire() {
		listeners.forEach(l -> l.geometricalObjectChanged(this));
	}
	
	@Override
	public String toString() {
		return String.format("Triangle(%s,%s)-(%s,%s)-(%s,%s), %s %s %s", x.x, x.y, y.x, y.y, z.x, z.y, bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
	}
}
