package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Complex implementira kompleksne brojeve i često korištene operacije nad njima
 * Sastoji se od privatnih double varijabli real i imaginary. Konstruktoru se
 * predaju varijable real i imaginary koje se više ne mogu mijenjati.
 * Rezultati operacija nad kompleksnim brojevima su novi primjerci 
 * razreda {@link Complex}
 * @author Luka Dragutin
 *
 */
public class Complex {

	/** realni dio kompleksnog broja */
	private double real;

	/** Imaginarni dio kompleksnog broja */
	private double imaginary;

	/** Broj 0 */
	public static final Complex ZERO = new Complex(0, 0);
	
	/** Broj 1 */
	public static final Complex ONE = new Complex(1, 0);
	
	/** Broj -1 */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	
	/** Kompleksni broj i */
	public static final Complex IM = new Complex(0, 1);

	/** Kompleksni broj -i */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Pomocna konstanta korištena za uspoređivanje dva kompleksna broja u metodi
	 * equals
	 */
	private static final double EQUALS_CONSTANT = 10E-10;

	/**
	 * Stvara novi primjerak razreda {@link Complex} 
	 * od realnog i imaginarnog dijela kompleksnog broja
	 * @param re Realni dio kompleksnog broja
	 * @param im Imaginarni dio kompleksnog broja
	 */
	public Complex(double re, double im) {
		this.real = re;
		this.imaginary = im;
	}

	/** vraća realni dio kompleksnog broja */
	public double getReal() {
		return real;
	}

	/** Vraća imaginarni dio kompleksnog broja */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * Računa i vraća apsolutnu vrijednost kompleksnog broja pomoću formule
	 * sqrt(real^2 + imaginary^2) gdje su real i imaginary varijable kompleksnog
	 * broja
	 * 
	 * @return double apsolutna vrijednost kompleksnog broja
	 */
	public double module() {
		return Math.sqrt(Math.pow(real, 2) + Math.pow(imaginary, 2));
	}

	/**
	 * Računa i vraća kut kompleksnog broja po polarnim koordinatama po formuli
	 * atan(imaginary/real)
	 * 
	 * @return double kut u radijanima
	 */
	private double getAngle() {
		double angle = Math.atan2(imaginary, real);
		if (angle < 0) {
			return angle + Math.PI * 2;
		} else {
			return angle;
		}
	}

	/**
	 * Zbraja this sa c kompleksnim brojem i vraca rezultat u obliku novog
	 * kompleksnog broja ne prihvaca null argumente
	 * 
	 * @param c Complex za zbrajanje
	 * @return Complex Novi kompleksni broj kao rezultat zbrajanja
	 * @throws NullPointerException ako je argument <code>null</code>
	 */
	public Complex add(Complex c) {
		Objects.requireNonNull(c);
		return new Complex(this.real + c.real, this.imaginary + c.imaginary);
	}

	/**
	 * Oduzima c kompleksni broj od this kompleksnog broja i vraća rezultat u obliku
	 * novog kompleksnog broja ne prihvaca null argumente
	 * 
	 * @param c ComplexNumber koji oduzimamo
	 * @return ComplexNumber Novi kompleksni broj kao rezultat oduzimanja
	 * @throws NullPointerException ako je argument <code>null</code>
	 */
	public Complex sub(Complex c) {
		Objects.requireNonNull(c);
		return new Complex(this.real - c.real, this.imaginary - c.imaginary);
	}

	/**
	 * Množi this i c kompleksne brojeve i vraća rezultat u obliku novog kompleknog
	 * broja ne prihvaca null argumente
	 * 
	 * @param c Complex koji množimo
	 * @return Complex Novi kompleksni broj kao rezultat množenja
	 * @throws NullPointerException ako je argument <code>null</code>
	 */
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c);
		double real = this.real * c.real - this.imaginary * c.imaginary;
		double imaginary = this.real * c.imaginary + this.imaginary * c.real;
		return new Complex(real, imaginary);
	}

	/**
	 * Dijeli this sa c kompleksnim brojem i vraća rezultat u obliku novog
	 * kompleksnog broja Ne prihvaća null argumente ni 0
	 * 
	 * @param c Complex djelitelj
	 * @return Complex Novi kompleksni broj kao rezultat dijeljenja
	 * @throws ArithmeticException ako argument c ima i realni i imaginarni dio 0
	 * @throws NullPointerException ako je argument null
	 */
	public Complex divide(Complex c) {
		Objects.requireNonNull(c);
		if (c.equals(ZERO)) {
			throw new ArithmeticException("Cannot divide by zero!");
		}
		double help = Math.pow(c.real, 2) + Math.pow(c.imaginary, 2);
		double real = (this.real * c.real + this.imaginary * c.imaginary) / help;
		double imaginary = (this.imaginary * c.real - this.real * c.imaginary) / help;
		return new Complex(real, imaginary);
	}

	/**
	 * Vraca novi kompleksni broj koji 
	 * je pomnozen sa -1
	 * @return Complex novi negirani kompleksni broj
	 */
	public Complex negate() {
		return ZERO.sub(this);
	}

	/**
	 * Potencira kompleksni broj na n-tu potenciju i vraća rezultat u obliku novog
	 * kompleksnog broja Ne prihvaća negativne argumente
	 * 
	 * @param n potencija na koju potenciramo kompleksni broj
	 * @return Complex Novi kompleksni broj kao rezultat potenciranja
	 * @throws IllegalArgumentException Ako je potencija negativna
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("The exponent has to be positive!");
		}
		double module = Math.pow(this.module(), n);
		double angle = n * this.getAngle();
		
		double real = module * Math.cos(angle);
		double imaginary = module * Math.sin(angle);
		
		return new Complex(real, imaginary);
	}

	/**
	 * Računa n-te korijene od kompleksnog broja te vraća sva riješenja u obliku
	 * liste kompleksnih brojeva veličine n
	 * 
	 * @throws IllegalArgumentException Prihvaća samo pozitivne argumente koji nisu
	 *                                  nula
	 * @param n Broj korijena koji racunamo
	 * @return Lista korijena kompleksnog broja (nultocke)
	 * @throws ArithmeticException ako argument nije veci od 0
	 */
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("The root has to be positive and not zero!");
		}
		Complex[] roots = new Complex[n];
		double magnitude = Math.pow(this.module(), (double) 1.0 / n);
		double angle = this.getAngle();
		
		for (int i = 0; i < n; i++) {
			double argument = (angle + 2 * i * Math.PI) / (double) n;
			double real = magnitude * Math.cos((argument));
			double imaginary = magnitude * Math.sin(argument);
			roots[i] = new Complex(real, imaginary);
		}
		
		return Arrays.asList(roots);
	}


	@Override
	public String toString() {
		return "(" + real + (imaginary >= 0 ? "+" : "-") + "i" + Math.abs(imaginary) + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(imaginary, real);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Complex))
			return false;
		Complex other = (Complex) obj;
		return Math.abs(imaginary - other.imaginary) < EQUALS_CONSTANT && Math.abs(real - other.real) < EQUALS_CONSTANT;
	}
}