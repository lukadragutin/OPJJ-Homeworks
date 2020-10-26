package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JColorChooser;
import javax.swing.JComponent;


/**
 * Swing komponenta koja omogućava biranje boje klikom miša na njeno područje.
 * Implementira sučelje {@link IColorProvider} i subjekt je promatračima tipa {@link ColorChangeListener} 
 * koji se obavijeste svaki put kad dođe do promjene boje.
 * 
 * @author LukaD
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Trenutna boja
	 */
	private Color selectedColor;
	
	/**
	 * Promatrači tipa {@link ColorChangeListener}
	 */
	private List<ColorChangeListener> listeners = new ArrayList<>();

	/**
	 * Visina komponente
	 */
	private final static int DEFAULT_HEIGHT = 15;
	
	/**
	 * Širina komponente
	 */
	private final static int DEFAULT_WIDTH = 15;
	
	
	public JColorArea(Color color) {
		selectedColor = Objects.requireNonNull(color);
		Container parent = getParent();

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				var d = JColorChooser.showDialog(parent, "Choose a color I dare you", selectedColor);
				if (d != null) {
					notifyListeners(JColorArea.this, selectedColor, d);
					selectedColor = d;
					repaint();
				}
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_HEIGHT, DEFAULT_WIDTH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(selectedColor);
		g2d.fillRect(0, 0, DEFAULT_HEIGHT, DEFAULT_WIDTH);
	}


	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Obavještava promatrače tipa {@link ColorChangeListener} o promjeni 
	 * boje. 
	 * @param source Izvor promjene (ovaj objekt)
	 * @param oldColor Stara boja
	 * @param newColor Nova boja
	 */
	public void notifyListeners(IColorProvider source, Color oldColor, Color newColor) {
		listeners.forEach(l -> l.newColorSelected(source, oldColor, newColor));
	}

}
