package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Razred koji predstavlja 2D vektore i definira operacije
 * tranlatacije, rotacije i skaliranja nad njima
 * @author Luka Dragutin
 *
 */
public class Vector2D {

	/**
	 * x koordinata vektora u koordinatnom sustavu
	 */
	private double x;
	
	/**
	 * y koordinata vektora u koordinatnom sustavu
	 */
	private double y;
	
	/**
	 * Stvara novi objekt razreda {@link Vector2D}
	 * @param x koordinata vektora
	 * @param y koordinata vektora
	 */
	public Vector2D(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter za x koordinatu vektora
	 * @return x koordinatu vektora
	 */
	public double getX() {
		return x;
	}

	/**
	 * Getter za y koordinatu vektora
	 * @return y koordinatu vektora
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Translatira vektor za vrijednost predanog vektora
	 * i sprema nove koordinate vektora
	 * @param offset vektor za ciju vrijednost translatiramo
	 * @throws NullPointerException ako je predani argument <code>null</code>
	 */
	public void translate(Vector2D offset) {
		Objects.requireNonNull(offset);
		 x += offset.x;
		 y += offset.y;
	}
	
	/**
	 * Stvara i vraca novi vektor koji je translatiran
	 * za vrijednost predanog vektora ne mijenjajuci
	 * vrijednost trenutnog vektora.
	 * @param offset vektor za ciju vrijednost translatiramo
	 * @return Vector2D sa translatiranim koordinatama
	 * @throws NullPointerException ako je predani argument <code>null</code>
	 */
	public Vector2D translated(Vector2D offset) {
		Vector2D translated = new Vector2D(this.x, this.y);
		translated.translate(offset);
		return translated;
	}
	
	/**
	 * Rotira vektor za vrijednost predanog kuta
	 * @param angle Kut za ciju vrijednost rotiramo vektor
	 */
	public void rotate(double angle) {
		double x1 = x;
		double y1 = y;
		x = x1*Math.cos(angle) - y1*Math.sin(angle);
		y = x1*Math.sin(angle) + y1*Math.cos(angle);
	}
	
	/**
	 * Stvara i vraca novi vektor koji je rotiran
	 * za vrijednost predanog kuta ne mijenjajuci
	 * vrijednost trenutnog vektora.
	 * @param angle Kut za ciju vrijednost rotiramo vektor
	 * @return Vector2D novi rotirani vektor
	 * za vrijednost predanog kuta
	 */
	public Vector2D rotated(double angle) {
		var rotated = new Vector2D(x, y);
		rotated.rotate(angle);
		return rotated;
	}
	
	/**
	 * Skalira, odnosno povecava vektor za vrijednost
	 * predanog argumenta
	 * @param scaler Koeficijent povecanja/smanjenja vektora
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}
	
	/**
	 * Stvara i vraca novi vektor koji je
	 * skaliran za vrijednost predanog argumenta scaler
	 * ne mijenjajuci vrijednost trenutnog vektora.
	 * @param scaler Koeficijent povecanja/smanjenja vektora
	 * @return Vector2D Novi vektor skaliran za vrijednost scaler
	 */
	public Vector2D scaled(double scaler) {
		var scaled = new Vector2D(x, y);
		scaled.scale(scaler);
		return scaled;
	}
	
	/**
	 * Stvara i vraca novi objekt razreda {@link Vector2D} koji je kopija
	 * vektora nad kojim je metoda pozvana
	 * @return Vector2D Kopiju vektora nad kojim pozivamo
	 * metodau
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
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
		if (!(obj instanceof Vector2D))
			return false;
		Vector2D other = (Vector2D) obj;
		return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
				&& Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
	}
	
	
	
	
}
