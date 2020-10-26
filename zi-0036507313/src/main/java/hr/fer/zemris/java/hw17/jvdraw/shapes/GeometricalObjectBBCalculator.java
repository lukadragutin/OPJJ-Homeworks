package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Point;
import java.awt.Rectangle;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Line;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Triangle;


/**
 * Razred koji implementira sučelje {@link GeometricalObjectVisitor} a računa najmanji pravokutnik
 * oko elemenata {@link DrawingModel}
 * @author LukaD
 *
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {

	/**
	 * najmanja x koordinata
	 */
	private int x1;
	
	/**
	 * najmanja y koordinata
	 */
	private int y1;
	
	/**
	 * Najveća x koordinata
	 */
	private int x2;
	
	/**
	 * Največa y koordinata
	 */
	private int y2;
	
	/**
	 * Ako točke pravokutnika nisu još postavljene, vrijednost je <code>false</code>
	 */
	private boolean set = false;
	
	@Override
	public void visit(Circle circle) {
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		calculateCircleBox(center, radius);
	}
	
	@Override
	public void visit(FilledCircle filledCircle) {
		calculateCircleBox(filledCircle.getCenter(), filledCircle.getRadius());
	}
	
	
	/**
	 * Provjerava prelazi li krug trenutne postavljene granice pravokutnika i ako
	 * prelazi onda postavlja granice na vrijednosti do kojih se proteže predani krug
	 * @param center Središte kruga
	 * @param radius Radijus kruga
	 */
	private void calculateCircleBox(Point center, int radius) {
		if(!set) {
			x1 = x2 = center.x;
			y1 = y2 = center.y;
			set = true;
			return;
		}
		if(center.x - radius < x1) {
			x1 = center.x - radius;
		}
		if(center.y - radius < y1) {
			y1 = center.y - radius;
		}
		if(center.x + radius > x2) {
			x2 = center.x + radius;
		}
		if(center.y + radius > y2 ) {
			y2 = center.y + radius;
		}
	}

	@Override
	public void visit(Line line) {
		checkPoint(line.getX());
		checkPoint(line.getY());
	}
	

	/**
	 * Provjerava je nalazi se točka unutar ili
	 * van pravokutnika i ako se nalazi izvan postavlja nove granice pravokutnika
	 * @param x Tocka koja se provjerava
	 */
	private void checkPoint(Point x) {
		if(!set) {
			x1 = x2 = x.x;
			y1 = y2 = x.y;
			set = true;
			return;
		}
		if(x.x < x1) {
			x1 = x.x;
		} else if(x.x > x2) {
			x2 = x.x;
		}
		
		if(x.y < y1) {
			y1 = x.y;
		} else if(x.y > y2) {
			y2 = x.y;
		}
	}
	
	/**
	 * Vrača pravokutnik nastao od točaka koje je izračunao
	 * {@link GeometricalObjectBBCalculator}
	 * @return {@link Rectangle} objekt koji predstavlja najmanji pravokutnik
	 * koji obuhvača sve elemente {@link DrawingModel}
	 */
	public Rectangle getBoundingBox() {
		if(!set) {
			throw new IllegalStateException("Box not set yet.");
		}
		return new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}

	@Override
	public void visit(Triangle triangle) {
		checkPoint(triangle.getX());
		checkPoint(triangle.getY());
		checkPoint(triangle.getZ());
	}
	
}
