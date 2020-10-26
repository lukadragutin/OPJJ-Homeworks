package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.JComponent;


/**
 * Komponenta grafickog sucelja koja crta
 * graf iz razreda {@link BarChart}.
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class BarChartComponent extends JComponent {

	/**Primjerak grafa koji vizualizira ovaj razred*/
	private BarChart chart;
	
	/**Najmanja komponenta na y osi*/
	private int yMin;

	/** Najveća komponenta na y osi */
	private int yMax;
	
	/**	Mapa koja cuva pozicije na komponenti
	 * pojedinih vrijednosti sa y osi */
	private HashMap<String, Point> mapY;
	
	/**	Mapa koja cuva pozicije na komponenti
	 * pojedinih vrijednosti sa x osi */
	private HashMap<String, Point> mapX;
	
	/** razmak izmedu dvije komponente na x osi*/
	private int xSpace;
	
	/** Razmak izmedu dvije komponente na y osi */
	private int ySpace;
	
	/**	Fiksni razmak naziva osi i samih osi grafa od ruba*/
	private static final int EDGE_SPACING = 10;
	
	/**
	 * Stvara novi primjerak razreda {@link BarChartComponent}
	 * @param chart Primjerak razreda {@link BarChart} koji 
	 * sadrzi podatke o grafu kojeg prikazuje ova komponenta
	 */
	public BarChartComponent(BarChart chart) {
		super();
		this.chart = Objects.requireNonNull(chart);

	}

	@Override
	protected void paintComponent(Graphics g) {
		var cp = this.getParent();
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform defaultAt = g2d.getTransform();
		
		//Rotiranje osi za pisanje vertikalno
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);

		
		g2d.setTransform(at);

		FontMetrics fm = g2d.getFontMetrics();
		g2d.drawString(chart.getyTitle(), -((cp.getHeight() / 2) + fm.stringWidth(chart.getyTitle())), EDGE_SPACING);

		//Vracanje osi
		g2d.setTransform(defaultAt);

		g2d.drawString(chart.getxTitle(), cp.getWidth() / 2 - (fm.stringWidth(chart.getxTitle()) / 2),
				cp.getHeight() - EDGE_SPACING);

		yMin = chart.getyMin();
		yMax = chart.getyMax();
		
		//Postavljanje gornje granice y vrijednosti grafa u slucaju da
		// Ymax - Ymin nije visekratnik Ystep na prvi iduci cijeli broj za koji to vrijedi
		while((yMax - yMin) % chart.getyStep() != 0) {
			yMax++;
		}
		
		calculateY(g2d);
		calculateX(g2d);
		
		//Crtanje 'tablica' na grafu
		var values = chart.getValues();
		var ySingleSpace = ySpace / chart.getyStep();
		var startY = mapY.get(String.valueOf(yMin));
		g2d.setColor(Color.ORANGE);
		for (var v : values) {
			g2d.fillRect(mapX.get(String.valueOf(v.getX())).x + 1, startY.y - (v.getY() - yMin) * ySingleSpace, xSpace - 1,
					(v.getY() - yMin) * ySingleSpace);

		}
	}

	//Pomocna metoda koja racuna i crta X vrijednosti i X os grafa
	private void calculateX(Graphics2D g2d) {
		var fm = g2d.getFontMetrics();

		//X os
		var start = mapY.get(String.valueOf(yMin));
		var end2 = new Point(this.getWidth() - EDGE_SPACING / 2, start.y);
		g2d.drawLine(start.x, start.y, end2.x, end2.y);

		var values = chart.getValues();
		mapX = new HashMap<>();

		//Vrijednosti na x osi i njihove pozicije
		int xLength = this.getWidth() - EDGE_SPACING - start.x;
		int xNumber = values.get(values.size() - 1).getX() - values.get(0).getX() + 1;
		xSpace = xLength / xNumber;
		int difference = values.get(values.size() - 1).getX() - xNumber;

		for (int i = 0; i < xNumber; i++) {
			mapX.put(String.valueOf(i + 1 + difference), new Point(start.x + i * xSpace, start.y));
			g2d.drawLine(start.x + i * xSpace, start.y, start.x + i * xSpace, start.y + fm.stringWidth(" "));
			g2d.setColor(Color.BLACK);
			g2d.drawString(String.valueOf(i + 1 + difference), start.x + i * xSpace + xSpace / 2,
					start.y + 2 * EDGE_SPACING);
			g2d.setColor(Color.GRAY);
		}
		//Strelica na osi
		int endX = start.x + xNumber * xSpace;
		g2d.drawLine(endX, start.y, endX, start.y + fm.stringWidth(" "));
		g2d.drawPolygon(new int[] { endX, endX, endX + 12 }, new int[] { start.y - 3, start.y + 3, start.y }, 3);

	}

	//Pomocna metoda koja racuna i crta Y vrijednosti i Y os grafa
	private void calculateY(Graphics2D g2d) {
		var fm = g2d.getFontMetrics();
		mapY = new HashMap<>();

		//Vrijednosti na y osi i njihove pozicije
		int yRange = yMax - yMin;
		int yNumber = yRange / chart.getyStep();
		ySpace = (this.getHeight() - 6 * EDGE_SPACING) / yNumber;
		int xCoordinate = fm.stringWidth(String.valueOf(yMax)) + 2 * EDGE_SPACING;

		for (int i = 0; i <= yNumber; i++) {
			String y = String.valueOf(yMax - i * chart.getyStep());
			mapY.put(y, new Point(xCoordinate + fm.stringWidth("  "), 2 * EDGE_SPACING + i * ySpace - fm.getDescent()));
			g2d.drawString(y, xCoordinate - fm.stringWidth(y), 2 * EDGE_SPACING + i * ySpace);
		}

		g2d.setColor(Color.GRAY);

		for (var entry : mapY.entrySet()) {
			var p = entry.getValue();
			g2d.drawLine(p.x, p.y, p.x - fm.stringWidth(" "), p.y);
		}

		//Y os i strelica na kraju osi
		var start = mapY.get(String.valueOf(yMin));
		var end = mapY.get(String.valueOf(yMax));
		g2d.drawLine(start.x, start.y, end.x, end.y - 5);
		g2d.drawPolygon(new int[] { end.x - 3, end.x + 3, end.x }, new int[] { end.y - 5, end.y - 5, end.y - 12 }, 3);

	}

}
