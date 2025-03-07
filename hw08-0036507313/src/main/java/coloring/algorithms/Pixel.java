package coloring.algorithms;

import java.util.Objects;

/**
 * Predstavlja jedan pixel na slici
 * @author Luka Dragutin
 *
 */
public class Pixel {

	/**
	 * x koordinata piksela (sirina)
	 */
	private int x;
	
	/**
	 * y koordinata piksela (visina)
	 */
	private int y;
	
	
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	

	public int getX() {
		return x;
	}



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
		if (!(obj instanceof Pixel))
			return false;
		Pixel other = (Pixel) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	
	
	
	
}
