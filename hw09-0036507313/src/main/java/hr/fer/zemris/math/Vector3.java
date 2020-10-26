package hr.fer.zemris.math;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * Razred koji implementira trodimenzionalne vektore
 * i modelira operacije nad njima. Rezultati operacija 
 * su uvijek novi primjerci razreda {@link Vector3}
 * @author Luka Dragutin
 *
 */
public class Vector3 {

	/**X koordinata vektora*/
	private double x;
	
	/**Y koordinata vektora*/
	private double y;
	
	/**Z koordinata vektora*/
	private double z;

	/** Format za ispis vektora */
	private static final NumberFormat FORMAT = new DecimalFormat("#0.000000");

	/**
	 * Konstruktor koji od x, y i z koordinata stvara i vraca
	 * novi vektor
	 * @param x X koordinata vektora
	 * @param y Y koordinata vektora
	 * @param z Z koordinata vektora
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** Vraca vrijednost x koordinate*/
	public double getX() {
		return x;
	}

	/**Vraca vrijednost y koordinate*/
	public double getY() {
		return y;
	}

	/** Vraca vrijednost z koordinate */
	public double getZ() {
		return z;
	}

	/**
	 * Racuna i vraca velicinu (duljinu) vektora
	 * @return double vrijednost duljine vektora
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Vraca normaliziranu verziju vektora(jedinici vektor)
	 * @return Novi jedinicni vektor nastao normalizacijom
	 * postojeceg vektora
	 */
	public Vector3 normalized() {
		double norm = this.norm();
		if (norm == 0) {
			return new Vector3(0, 0, 0);
		}
		return new Vector3(x / norm, y / norm, z / norm);
	}

	/**
	 * Zbraja vektor za drugim vektorom i 
	 * vraca novi vektor kao razultat zbrajanja
	 * @param other Vektor koji pribrajamo
	 * @return Novi primjerak razreda {@link Vector3} 
	 * kao rezultat zbrajanja
	 * @throws {@link NullPointerException} ako je argument <code>null</code>
	 */
	public Vector3 add(Vector3 other) {
		Objects.requireNonNull(other);
		return new Vector3(x + other.getX(), y + other.getY(), z + other.getZ());
	}

	/**
	 * Oduzima drugi vektorom i 
	 * vraca novi vektor kao razultat zbrajanja
	 * @param other Vektor koji oduzimamo
	 * @return Novi primjerak razreda {@link Vector3} 
	 * kao rezultat oduzimanja
	 * @throws {@link NullPointerException} ako je argument <code>null</code>
	 */
	public Vector3 sub(Vector3 other) {
		Objects.requireNonNull(other);
		return new Vector3(x - other.getX(), y - other.getY(), z - other.getZ());
	}

	/**
	 * Racuna kosinus kuta izmedu vektora
	 * @param other Vektor koji zatvara s ovim vektorom
	 * kut ciji kosinus racunamo
	 * @return double vrijednost kosinusa kuta
	 */
	public double cosAngle(Vector3 other) {
		return this.dot(other) / (this.norm() * other.norm());
	}

	/**
	 * Racuna skalarni umnozak dvaju vektora
	 * @return double vrijednost kao skalarni umnozak
	 */
	public double dot(Vector3 other) {
		return x * other.getX() + y * other.getY() + z * other.getZ();
	}

	/**
	 * Vektorski umnozak dvaju vektora
	 * @param other Vektor s kojima mnozimo
	 * @return Novi primjerak razreda {@link Vector3} kao
	 * rezultat mnozenja
	 */
	public Vector3 cross(Vector3 other) {
		double x = this.y * other.getZ() - this.z * other.getY();
		double y = this.z * other.getX() - this.x * other.getZ();
		double z = this.x * other.getY() - this.y * other.getX();
		return new Vector3(x, y, z);
	}

	/**
	 * Mnozi vektor sa konstantom
	 * @param s Konstanta tipa double s kojom mnozimo vektor
	 * @return Vektor pomnozen sa konstantom iz argumenta
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * Vraca vektor kao polje svojih koordinata
	 * @return Polje koordinata vektora
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	@Override
	public String toString() {
		return "(" + FORMAT.format(x) + "," + FORMAT.format(y) + "," + FORMAT.format(z) + ")";
	}
}
