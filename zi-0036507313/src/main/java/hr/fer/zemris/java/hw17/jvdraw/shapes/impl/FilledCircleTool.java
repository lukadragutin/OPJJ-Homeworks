package hr.fer.zemris.java.hw17.jvdraw.shapes.impl;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Tool;


/**
 * Implementira sučelje {@link Tool} tako da omogućava crtanje
 * primjeraka razreda {@link FilledCircle}
 * @author LukaD
 *
 */
public class FilledCircleTool extends CircleTool {

	/**
	 * Dobavljač boje pozadine
	 */
	private IColorProvider bgProvider;

	public FilledCircleTool(DrawingModel dm, JDrawingCanvas jdc, IColorProvider fgProvider, IColorProvider bgProvider) {
		super(dm, jdc, fgProvider);
		this.bgProvider = Objects.requireNonNull(bgProvider);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (center == null) {
			center = e.getPoint();
		} else {
			Point now = e.getPoint();
			radius = (int) Point.distance(center.x, center.y, now.x, now.y);
			if (radius > 0) {
				dm.add(new FilledCircle(center, radius, fgProvider.getCurrentColor(), bgProvider.getCurrentColor()));
				center = null;
				radius = 0;
			}
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (center != null || radius > 0) {
			g2d.setColor(bgProvider.getCurrentColor());
			g2d.fillOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
			g2d.setColor(fgProvider.getCurrentColor());
			g2d.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
		}
	}

}
