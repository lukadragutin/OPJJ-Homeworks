package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.math.*;

/**
 * Razred koji modelira "kornjaču" L-sustava.
 * @author Luka Dragutin
 */
public class TurtleState {

	/**Vektor trenutne pozicije u koordinatnom sustavu*/
	private Vector2D position;
	
	/**Vektor orijentacije*/
	private Vector2D orientation;
	
	/**Boja kojom crta ova "kornjača" */
	private Color color;
	
	/** Jedinična vrijednost kojom se kreće*/
	private double unitLength;
	
	/**
	 * Stvara novu "kornjaču" iz zadanih parametara.
	 * @param position Početna pozicija kornjače
	 * @param orientation Početna orijentacija kornjače
	 * @param color Boja kojom kornjača crta
	 * @param unitLength Jedinična vrijednost kojom se kreće
	 * @throws NullPointerException ako je neki od parametara
	 * {@code position orientation color} jednak <code>null</code>
	 */
	public TurtleState(Vector2D position, Vector2D orientation, Color color, double unitLength) {
		super();
		this.position = Objects.requireNonNull(position);
		this.orientation = Objects.requireNonNull(orientation);
		this.color = Objects.requireNonNull(color);
		this.unitLength = unitLength;
	}
	
	
	public Vector2D getPosition() {
		return position;
	}
	
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	public Vector2D getOrientation() {
		return orientation;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public double getUnitLength() {
		return unitLength;
	}
	
	public void setUnitLength(double unitLength) {
		this.unitLength = unitLength;
	}

	/**
	 * Vraca novi primjerak objekta koji je kopija ovog
	 * @return Kopija ove "kornjače"
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), orientation.copy()
				, new Color(color.getRGB()), unitLength);
	}
	
	
}
