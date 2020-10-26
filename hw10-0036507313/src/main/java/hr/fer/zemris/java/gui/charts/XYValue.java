package hr.fer.zemris.java.gui.charts;

import java.util.Objects;


/**
 * Razred koji modelira jednu vrijednost u 
 * razredu {@link BarChart} odredene x i y komponentama
 * @author Luka Dragutin
 *
 */
public class XYValue {

	/**
	 * X komponenta vrijednosti
	 */
	private int x;
	
	/** Y komponenta vrijednosti*/
	private int y;
	
	
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/** Getter za {@link #x}*/
	public int getX() {
		return x;
	}

	/** Getter za {@link #y}*/
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof XYValue))
			return false;
		XYValue other = (XYValue) obj;
		return x == other.x && y == other.y;
	}
	
	
}
