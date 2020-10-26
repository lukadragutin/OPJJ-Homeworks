package hr.fer.zemris.java.hw02;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.lang.Math;

/**
 * ComplexNumbers implementira kompleksne brojeve i često korištene operacije nad njima
 * Sastoji se od privatnih double varijabli real i imaginary
 * Konstruktoru se predaju varijable real i imaginary koje se više ne mogu mijenjati
 * @author Luka Dragutin
 *
 */
public class ComplexNumber {
	
	/** realni dio kompleksnog broja*/
	private double real;
	
	/**Imaginarni dio kompleksnog broja*/
	private double imaginary;
	
	/**Pomocna konstanta korištena za uspoređivanje dva kompleksna
	   broja u metodi equals*/
	private static final double EQUALS_CONSTANT = 10E-10;
	
	public ComplexNumber(double real, double imaginary) {
		this.real = Objects.requireNonNull(real);
		this.imaginary = Objects.requireNonNull(imaginary);
	}
	
	/**
	 * Stvara i vraća novi kompleksni broj sa realnom vrijednošću postavljenom 
	 * na vrijednost argumenta real, a imaginarnu na 0
	 * @param real
	 * @return new ComplexNumber(real, 0)
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}
	
	/**
	 * Stvara i vraća novi kompleksni broj sa imaginarnom vrijednošću postavljenom
	 * na vrijednost argumenta imaginary, a realnu na 0
	 * @param imaginary
	 * @return new ComplexNumber(0, imaginary)
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	 /**
	  * Stvara i vraća novi kompleksni broj dobiven preračunavanjem iz polarnih koordinata
	  * @param magnitude Apsolutna vrijednost kompleksnog broja u polarnim koordinatama
	  * @param angle Kut ili argument kompleksnog broja u polarnim koordinatama
	  * @return
	  */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	/**
	 * Iz String zapisa kompleksnog broja izvlači realne i imaginarne vrijednosti,
	 * te pomoću njih stvara i vraća novi kompleksni broj
	 * @throws IllegalArgumentException Pri slučaju predavanja krivo 
	 * String zapisa kompleksnog broja kao argument
	 * @param s String zapis kompleksnog broja bez razmaka 
	 * npr. "9.2-4i" - prvo realni pa imaginarni dio
	 * @return ComplexNumber
	 */
	public static ComplexNumber parse(String s) {
		boolean i = false;
		if(s.equals("i") || s.equals("+i")) {
			return fromImaginary(1);
		}
		else if(s.equals("-i")) {
			return fromImaginary(-1);
		}
		try {
			String parse = s;
			if(s.endsWith("i") && !s.endsWith("+i") && !s.endsWith("-i")) {
				parse = s.substring(0, s.length()-2);
				i = true;
			}
			double number = Double.parseDouble(parse);
			if(i) {
				return fromImaginary(number);
			}
			else {
				return fromReal(number);
			}
		} catch (NumberFormatException ex) {
		}
		Pattern pattern = Pattern.compile("([+-]?\\d*(\\.\\d+)?)([+-]?\\d*(\\.\\d+)?)i");
		Matcher matcher = pattern.matcher(s);
		if(matcher.matches()) {
			if(s.endsWith("+i")) {
				return new ComplexNumber(Double.parseDouble(matcher.group(1)), 1);
			}
			else if(s.endsWith("-i")) {
				return new ComplexNumber(Double.parseDouble(matcher.group(1)), -1);
			}
			double real = Double.parseDouble(matcher.group(1));
			double imaginary = Double.parseDouble(matcher.group(3));
			return new ComplexNumber(real, imaginary);
		}
		else {
			throw new IllegalArgumentException("Not a valid, parseable complex number format!");
		}
	}
	
	/** vraća realni dio kompleksnog broja*/
	public double getReal() {
		return real;
	}
	
	/**Vraća imaginarni dio kompleksnog broja*/
	public double getImaginary() {
		return imaginary;
	}
	
	/**Računa i vraća apsolutnu vrijednost kompleksnog broja
	 * pomoću formule sqrt(real^2 + imaginary^2) gdje su real i imaginary
	 * varijable ompleksnog broja
	 * @return double
	 */
	public double getMagnitude() {
		return Math.sqrt(Math.pow(real, 2) + Math.pow(imaginary, 2));
	}
	
	/** Računa i vraća kut kompleksnog broja po polarnim koordinatama
	 * po formuli atan(imaginary/real)
	 * @return double
	 */
	public double getAngle() {
		double angle = Math.atan2(imaginary, real);
		if(angle < 0) {
			return angle + Math.PI*2;
		}
		else {
			return angle;
		}
	}
	
	/**Zbraja this sa c kompleksnim brojem i vraca
	 * rezultat u obliku novog kompleksnog broja
	 * ne prihvaca null argumente
	 * @param c ComplexNumber za zbrajanje
	 * @return ComplexNumber
	 */
	public ComplexNumber add(ComplexNumber c) {
		Objects.requireNonNull(c);
		return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
	}
	
	/** Oduzima c kompleksni broj od this kompleksnog broja
	 * i vraća rezultat u obliku novog kompleksnog broja
	 * ne prihvaca null argumente
	 * @param c ComplexNumber koji oduzimamo
	 * @return ComplexNumber
	 */
	public ComplexNumber sub(ComplexNumber c) {
		Objects.requireNonNull(c);
		return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
	}
	
	/** Množi this i c kompleksne brojeve i vraća rezultat u
	 * obliku novog kompleknog broja
	 * ne prihvaca null argumente
	 * @param c ComplexNumber koji množimo
	 * @return ComplexNumber
	 */
	public ComplexNumber mul(ComplexNumber c) {
		Objects.requireNonNull(c);
		double real = this.real * c.real - this.imaginary * c.imaginary;
		double imaginary = this.real * c.imaginary + this.imaginary * c.real;
		return new ComplexNumber(real, imaginary);
	}
	
	/** Dijeli this sa c kompleksnim brojem i 
	 * vraća rezultat u obliku novog kompleksnog broja
	 * Ne prihvaća null argumente ni 0
	 * @throws ArithmeticException ako argument c
	 * ima i realni i imaginarni dio 0
	 * @param c ComplexNumber djelitelj
	 * @return ComplexNumber
	 */
	public ComplexNumber div(ComplexNumber c) {
		Objects.requireNonNull(c);
		if(c.real == 0 && c.imaginary == 0) {
			throw new ArithmeticException("Cannot divide by zero!");
		}
		double help = Math.pow(c.real, 2) + Math.pow(c.imaginary, 2);
		double real = (this.real * c.real + this.imaginary * c.imaginary)/help;
		double imaginary = (this.imaginary * c.real - this.real * c.imaginary)/help;
		return new ComplexNumber(real, imaginary);
	}
	
	/** Potencira kompleksni broj na n-tu potenciju i vraća 
	 * rezultat u obliku novog kompleksnog broja
	 * Ne prihvaća negativne ni null argumente
	 * @param n potencija na koju potenciramo kompleksni broj
	 * @return ComplexNumber
	 */
	public ComplexNumber power(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("The exponent has to be positive!");
		}
		Objects.requireNonNull(n);
		double magnitude = Math.pow(this.getMagnitude(), n);
		double angle = n * this.getAngle();
		double real = magnitude * Math.cos(angle);
		double imaginary = magnitude * Math.sin(angle);
		return new ComplexNumber(real, imaginary);
	}
	
	/**
	 * Računa n-te korijene od kompleksnog broja te vraća sva
	 * riješenja u obliku polja kompleksnih brojeva veličine n
	 * @throws IllegalArgumentException 
	 * Prihvaća samo pozitivne argumente koji nisu nula
	 * @param n 
	 * @return ComplexNumber[n]
	 */
	public ComplexNumber[] root(int n) {
		if(n <= 0) {
			throw new IllegalArgumentException("The root has to be positive and not zero!");
		}
		ComplexNumber[] roots = new ComplexNumber[n];
		double magnitude = Math.pow(this.getMagnitude(), (double) 1.0/n);
		double angle = this.getAngle();
		for(int i = 0; i < n; i++) {
			double argument = (angle + 2 * i * Math.PI)/(double) n;
			double real = magnitude * Math.cos((argument));
			double imaginary = magnitude * Math.sin(argument);
			roots[i] = new ComplexNumber(real, imaginary);
		}
		return roots;
	}
	/** Vraća kompleksni broj u String formatu
	 */
	public String toString() {
		if(imaginary == 0) {
			return Objects.toString(real);
		}
		else if(real == 0) {
			return Objects.toString(imaginary) + "i";
		}
		else {
			if(imaginary < 0) {
				return Objects.toString(real) + " " + Objects.toString(imaginary) + "i";
			}
			else {
				return Objects.toString(real) + " + " + Objects.toString(imaginary) + "i";
			}
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(imaginary, real);
	}
	/**
	 * Gleda jesu li neka dva kompleksna broja ista, uspoređujući im
	 * realne i imaginarne dijelove
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ComplexNumber))
			return false;
		ComplexNumber other = (ComplexNumber) obj;
		return Math.abs(imaginary - other.imaginary) < EQUALS_CONSTANT &&
				Math.abs(real - other.real) < EQUALS_CONSTANT;
	}
}