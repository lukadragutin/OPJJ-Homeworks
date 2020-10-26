package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Razred koji implementira kompleksne polinome i operacije
 * nad njima. 
 * @author Luka Dragutin
 *
 */
public class ComplexPolynomial {

	/**
	 * Kompleksni koeficijenti polinoma kojima indeks odgovara
	 * redu varijable koju mnoze
	 */
	private List<Complex> factors;

	public ComplexPolynomial(Complex ...factors) {
		this.factors = Arrays.asList(Objects.requireNonNull(factors));
	}

	/** Vraca stupanj polinoma kao short */
	public short order() {
		return (short) (factors.size() - 1);
	}

	/**
	 * Mnozi dva polinoma i vraca rezultat kao 
	 * novi primjerak razreda {@link ComplexPolynomial}
	 * @param p Polinom kojim se mnozi
	 * @return {@link ComplexPolynomial} Rezultat mnozenja
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		ArrayList<Complex> multiplyed = new ArrayList<>();
		int newOrder = this.order() + p.order();
		for (int i = 0; i <= newOrder; i++) {
			multiplyed.add(Complex.ZERO);
		}
		int i = 0;
		for (Complex factor1 : factors) {
			for (int j = 0;j < p.factors.size();j++) {
				multiplyed.set(i + j, multiplyed.get(i + j).add(factor1.multiply(p.factors.get(j))));
			}
			i++;
		}
		return new ComplexPolynomial(multiplyed.toArray(new Complex[newOrder + 1]));
	}

	/**
	 * Racuna i vraca derivaciju kompleksnog polinoma
	 * kao novi primjerak razreda {@link ComplexPolynomial}
	 * @return Derivaciju polinoma
	 */
	public ComplexPolynomial derive() {
		List<Complex> newFactors = new ArrayList<>();
		int i = 0;
		for (Complex c : factors) {
			newFactors.add(c.multiply(new Complex(i, 0)));
			i++;
		}
		newFactors.remove(0);
		return new ComplexPolynomial(newFactors.toArray(new Complex[newFactors.size()]));
	}

	/**
	 * Uvrstava kompleksni broj iz argumenta
	 * kao tocku polinoma i racuna i vraca rezultat
	 * @param z Kompleksna tocka za koju se trazi vrijednost
	 * @return Vrijednost polinoma za tocku z
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		int i = 0;
		for (Complex c : factors) {
			result = result.add(z.power(i).multiply(c));
			i++;
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = factors.size() - 1; i >= 0; i--) {
			if (i == 0) {
				sb.append(factors.get(i).toString());
			} else {
				sb.append(factors.get(i).toString() + "*z^" + i + "+");			
			}
		}
		return sb.toString();
	}
}
