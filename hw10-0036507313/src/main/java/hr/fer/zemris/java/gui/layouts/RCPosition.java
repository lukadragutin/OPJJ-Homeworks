package hr.fer.zemris.java.gui.layouts;

/**
 * Razred koji modelira pozicije komponenti 
 * u razredu {@link CalcLayout}
 * @author Luka Dragutin
 *
 */
public class RCPosition {

	/**
	 * X koordinata pozicije komponente
	 */
	private int x;
	
	/** Y koordinata pozicije komponente */
	private int y;
	
	
	/**
	 * Stvara novi primjerak {@link RCPosition}
	 * @param x X koordinata
	 * @param y Y koordinata
	 * @throws CalcLayoutException
	 * <i> Ako se pokusaju dodati komponente sa koordinatama x = 1, 1 < y < 6
	 * <i> Ako se pokusaju dodati komponente sa koordinatama manjim od 1 ili x > 5, y > 7
	 */
	public RCPosition(int x, int y) {
		if(x > 5 || x < 1) {
			throw new CalcLayoutException("Illegal position!");
		}
		if(y < 1 || y > 7) {
			throw new CalcLayoutException("Illegal position!");
		}
		if(x == 1 && (y > 1 && y < 6)) {
			throw new CalcLayoutException("Illegal position!");
		}
		this.x = x;
		this.y = y;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}
	
	
}
