package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.JVDraw;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Tool;

/**
 * Implementacija sučelja {@link Tool}  koja pruža
 * mogućnosti crtanja linija na komponenti za crtanje aplokacije
 * {@link JVDraw} {@link JDrawingCanvas}
 * @author LukaD
 *
 */
public class LineTool implements Tool {

	/**
	 * Model gdje se pohranjuju novonastale linije
	 */
	private DrawingModel dm;
	
	/**
	 * Komponenta po kojoj se crtaju objekti
	 */
	private JDrawingCanvas jdc;
	
	/**
	 * Dobavljač boje linije
	 */
	private IColorProvider fgProvider;

	/**
	 * Prva točka linije
	 */
	private Point x;
	
	/**
	 * Druga točka linije
	 */
	private Point y;
	
	/**
	 * Trenutna pozicija pokazivača miša
	 */
	private Point position;
	

	public LineTool(DrawingModel dm, JDrawingCanvas jdc, IColorProvider fgProvider) {
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
		if (x == null) {
			x = e.getPoint();
		} else {
			y = e.getPoint();
			dm.add(new Line(x, y, fgProvider.getCurrentColor()));
			x = null;
			y = null;
			position = null;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		position = e.getPoint();
		jdc.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void paint(Graphics2D g2d) {
		g2d.setColor(fgProvider.getCurrentColor());
		if (y != null) {
			g2d.drawLine(x.x, x.y, y.x, y.y);
		} else if (x != null && position != null) {
			g2d.drawLine(x.x, x.y, position.x, position.y);
		}
	}

}
