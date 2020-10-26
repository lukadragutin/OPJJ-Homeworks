package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Tool;

public class TriangleTool implements Tool {

	private Point x;
	private Point y;
	private Point z;
	private Point now;
	private IColorProvider fgColorProvider;
	private IColorProvider bgColorProvider;
	private DrawingModel dm;
	private JDrawingCanvas jdc;

	public TriangleTool(IColorProvider fgColorProvider, IColorProvider bgColorProvider, DrawingModel dm,
			JDrawingCanvas jdc) {
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;
		this.dm = dm;
		this.jdc = jdc;
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
		} else if (y == null) {
			y = e.getPoint();
		} else {
			z = e.getPoint();
			dm.add(new Triangle(x, y, z, fgColorProvider.getCurrentColor(), bgColorProvider.getCurrentColor()));
			x = null;
			y = null;
			z = null;
			now = null;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (x == null)
			return;
		else
			now = e.getPoint();
		jdc.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void paint(Graphics2D g2d) {
		if(x == null) return;
		else if(y == null) {
			g2d.setColor(fgColorProvider.getCurrentColor());
			g2d.drawLine(x.x, x.y, now.x, now.y);
		} else {
			g2d.setColor(bgColorProvider.getCurrentColor());
			g2d.fillPolygon(new int[] {x.x, y.x, now.x}, new int[] {x.y,y.y,now.y} , 3);
			
			g2d.setColor(fgColorProvider.getCurrentColor());
			g2d.drawPolygon(new int[] {x.x, y.x, now.x}, new int[] {x.y,y.y,now.y} , 3);
		}
	}

}
