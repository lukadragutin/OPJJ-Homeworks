package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.Scanner;

import hr.fer.zemris.java.fractals.lexer.ComplexLexer;
import hr.fer.zemris.java.fractals.lexer.TokenType;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program koji računa i crta vizutalnu reprezentaciju Newton-Raphsonovih fraktala.
 * Pri pokretanju trazi od korisnika da unese barem dvije nultocke kompleksnog polinoma
 * kojem se racunaju Newton-Raphsonovi fraktali te naposljetku i nacrtaju.
 * npr. 2 + i2 (samo realne ili samo imaginarne vrijednosti su isto valjane)
 * @author Luka Dragutin
 */
public class Newton {

	public static void main(String[] args) {

		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n"
				+ "Please enter at least two roots, one root per line. Enter 'done' when done.");

		System.out.print("Root 1> ");
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		
		Complex root = null;
		ArrayList<Complex> roots = new ArrayList<>();
		
		while (!line.equals("done")) {
			if (line.isBlank()) {
				System.out.println("Cannot enter empty sequence!");
				System.out.print("Root " + (roots.size() + 1) + "> ");
				line = sc.nextLine();
				continue;
			}
			try {
				root = parseComplex(line);
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
				System.out.print("Root " + (roots.size() + 1) + "> ");
				line = sc.nextLine();
				continue;
			}
			roots.add(root);
			System.out.print("Root " + (roots.size() + 1) + "> ");
			line = sc.nextLine();
		}
		sc.close();
		
		if(roots.size() < 2) {
			System.out.println("Did not enter enough roots... Exiting!");
			return;
		}
		
		System.out.println("Selected roots: ");
		roots.forEach(System.out::println);
		
		System.out.println("Image of fractal will appear shortly. Thank you.");
		
		ComplexRootedPolynomial c = new ComplexRootedPolynomial(Complex.ONE, roots.toArray(new Complex[roots.size()]));
		
		FractalProducerImpl fp = new FractalProducerImpl(c); 
		FractalViewer.show(fp);
	}

	/**
	 * Metoda za parsiranje kompleksnih brojeva iz tekstualnog zapisa
	 * @param line tekstualni zapis kompleksnog broja
	 * @return Kompleksni broj iz tekstualnog zapisa kao primjerak
	 * razreda {@link Complex}
	 * @throws RuntimeException Ako je krivi format zapisa kompleksnog broja
	 */
	public static Complex parseComplex(String line) {
		ComplexLexer lexer = new ComplexLexer(line);
		var tokenList = lexer.getTokenList();
		var token = tokenList.get(0);
		
		int counter = 0;
		double[] values = new double[2];
		
		do {
			
			if (counter >= 2) {
				throw new RuntimeException("Too many arguments!");
			}
			
			boolean im = false;
			boolean neg = false;
			
			if (token.getType().equals(TokenType.OPERATOR)) {
				tokenList.remove(0);
				if (token.getValue().equals("-")) {
					neg = true;
				}
			
				token = tokenList.get(0);
				if (token.getType().equals(TokenType.OPERATOR)) {
					throw new RuntimeException("Invalid complex format!");
				}
			}
			
			if (token.getType().equals(TokenType.I)) {
				im = true;
				tokenList.remove(0);
				token = tokenList.get(0);
				
				if (token.getType().equals(TokenType.I) || token.getType().equals(TokenType.OPERATOR)) {
					throw new RuntimeException("Invalid complex format!");
				}
			}
			
			if (token.getType().equals(TokenType.NUMBER)) {
				if (!im && counter >= 1) {
					throw new RuntimeException("Real part of complex number comes first!");
				}
				
				int i = 1;
				if (!im) {
					i = 0;
				}
				double value = Double.parseDouble(token.getValue());
				values[i] = neg ? -value : value;
				
				tokenList.remove(0);
				token = tokenList.get(0);
				
				if (token.getType().equals(TokenType.NUMBER) || token.getType().equals(TokenType.I)) {
					throw new RuntimeException("Invalid complex format!");
				}
			} else if (im) {
				values[1] = neg ? -1 : 1;
			}
			counter++;
		} while (!token.getType().equals(TokenType.EOF));
		return new Complex(values[0], values[1]);
	}
}
