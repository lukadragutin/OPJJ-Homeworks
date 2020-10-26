package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Tool;


/**
 * Implementira sučelje {@link Tool} i nudi API za
 * crtanje objekata {@link Circle} na komponenti {@link JDrawingCanvas}
 * @author LukaD
 *
 */
public class CircleTool implements Tool {

	/**
	 * Model u koji se pohranjuju novonacrtani objekti
	 */
	protected DrawingModel dm;
	
	/**
	 * Komponenta za crtanje i prikaz novih objekata
	 */
	protected JDrawingCanvas jdc;
	
	/**
	 * Dobavljač boje
	 */
	protected IColorProvider fgProvider;
	
	/**
	 * Središte kruga koji se crta
	 */
	protected Point center;
	
	/**
	 * Polumjer kruga koji se crta
	 */
	protected int radius;

	public CircleTool(DrawingModel dm, JDrawingCanvas jdc, IColorProvider fgProvider) {
		this.dm = dm;
		this.jdc = jdc;
		this.fgProvider = fgProvider;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (center == null) {
			center = e.getPoint();
		} else  {
			Point now = e.getPoint();
			radius = (int) Point.distance(center.x, center.y, now.x, now.y);
			dm.add(new Circle(center, radius, fgProvider.getCurrentColor()));
			center = null;
			radius = 0;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point now = e.getPoint();
		if (center != null) {
			radius = (int) Point.distance(center.x, center.y, now.x, now.y);
			jdc.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (center == null || radius == 0)
			return;
		g2d.setColor(fgProvider.getCurrentColor());
		g2d.drawOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
	}

}
