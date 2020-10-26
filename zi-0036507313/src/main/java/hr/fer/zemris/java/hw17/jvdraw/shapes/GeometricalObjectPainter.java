package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Line;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Triangle;


/**
 * Razred koji implementira sučelje {@link GeometricalObjectVisitor} tako da
 * preko objekta {@link Graphics2D} dobivenog kroz kontruktor, nacrta pojedini
 * objekt {@link GeometricalObject}
 * @author LukaD
 *
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {

	/**
	 * Primjerak {@link Graphics2D} objekta korištenog za crtanje 
	 * objekata {@link GeometricalObject}
	 */
	private Graphics2D g2d;
	
	public GeometricalObjectPainter(Graphics2D g2d) {
		this.g2d = Objects.requireNonNull(g2d);
	}
	
	/**
	 * Crta liniju
	 */
	@Override
	public void visit(Line line) {
		g2d.setColor(line.getColor());
		g2d.drawLine(line.getX().x, line.getX().y, line.getY().x, line.getY().y);
	}

	/**
	 * Crta krug
	 */
	@Override
	public void visit(Circle circle) {
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		g2d.setColor(circle.getColor());
		g2d.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
	}

	/**
	 * Crta ispunjeni krug
	 */
	@Override
	public void visit(FilledCircle filledCircle) {
		Point center = filledCircle.getCenter();
		int radius = filledCircle.getRadius();
		g2d.setColor(filledCircle.getColor());
		g2d.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
		g2d.setColor(filledCircle.getBgColor());
		g2d.fillOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
	}

	@Override
	public void visit(Triangle triangle) {
		g2d.setColor(triangle.getBgColor());
		Point x = triangle.getX();
		Point y = triangle.getY();
		Point z = triangle.getZ();
		g2d.fillPolygon(new int[] {x.x, y.x, z.x}, new int[] {x.y,y.y,z.y} , 3);
		
		g2d.setColor(triangle.getColor());
		g2d.drawPolygon(new int[] {x.x, y.x, z.x}, new int[] {x.y,y.y,z.y} , 3);
	}
	
	

	
}
