package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Razred koji implementira kompleksni polinom
 * zapisan kao umnozak nultocki kompleksnog polinoma
 * @author Luka Dragutin
 *
 */
public class ComplexRootedPolynomial {

	/** Konstanta kojom je pomnozen polinom */
	private Complex constant;
	
	/** Lista nultocki polinoma */
	private List<Complex> roots;

	/**
	 * Konstruktor razreda {@link ComplexRootedPolynomial} koji
	 * prima konstantu i nultocke polinoma
	 * @param constant Konstanta polinoma
	 * @param roots Lista nultocki polinoma
	 */
	public ComplexRootedPolynomial(Complex constant, Complex... roots) {
		this.constant = constant;
		this.roots = Arrays.asList(roots);
	}

	/**
	 * Uvrstava kompleksni broj predan kao argument u
	 * polinom i racuna vrijednost za zadani broj
	 * @param z Kompleksni broj za koji se racuna vrijednost
	 * @return Vrijednost polinoma u tocki z
	 */
	public Complex apply(Complex z) {
		Complex result = constant.add(Complex.ZERO);
		for (Complex c : roots) {
			result = result.multiply(z.sub(c));
		}
		return result;
	}

	/**
	 * Pretvara zapis polinoma kao umnoska nultocki
	 * u obican kompleksni polinom 
	 * @return Kompleksni polinom
	 */
	public ComplexPolynomial toComplexPolynom() {
		List<ComplexPolynomial> complexParts = new ArrayList<>();
		complexParts.add(new ComplexPolynomial(constant));
		for (Complex c : roots) {
			complexParts.add(new ComplexPolynomial(c.negate(), Complex.ONE));
		}
		ComplexPolynomial result = new ComplexPolynomial(Complex.ONE);
		for (ComplexPolynomial cp : complexParts) {
			result = result.multiply(cp);
		}
		return result;
	}

	/**
	 * Vraca indeks nultocke(poziciju nultocke u listi nultocki) koja
	 * je najbliza predanom kompleksnom broju a da je odstupanje unutar
	 * odredene granice
	 * @param z Kompleksni broj koji provjeravamo
	 * @param threshold Granica odstupanja
	 * @return Indeks nultocke koja je najbliza
	 * vrijednosti predanog kompleksnog broja, ili -1 ako nijedna
	 * nije unutar odstupanja
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		int index = -1;
		double closest = threshold;
		int i = 0;
		for (Complex c : roots) {
			double result = z.sub(c).module();
			if (result < closest) {
				index = i;
				closest = result;
			}
			i++;
		}
		return index;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(constant.toString());
		for (Complex c : roots) {
			sb.append("*(z-" + c.toString() + ")");
		}
		return sb.toString();
	}
}
