package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Application for calculating the area and perimeter of a rectangle
 * Parameters can be entered through command line while 
 * starting the application or when asked by the user interface.
 * 
 * @author Luka Dragutin
 * @version 1.0
 */
public class Rectangle {
	/**
	 * The main executable, starts the application
	 * @param args The width and height of a rectangle
	 * Can be entered when the application starts 
	 */
	public static void main(String[] args) {
		
		if(args.length != 0 && args.length != 2) {
			System.out.println("Unijeli ste pogrešan broj argumenata.");
			return;
		}
		if(args.length == 2) {
			try {
				calculateAreaPerimeter(parseAndCheckPositivity(args[0]),parseAndCheckPositivity(args[1]));
				return;
			} catch (NumberFormatException ex) {
				System.out.println("Zadani argumenti nisu valjani decimalni brojevi");
				return;
			} catch (IllegalArgumentException ex) {
				System.out.format("%s%n", ex.getMessage());
				return;
			}
		}
		calculateAreaPerimeter(askSide("širinu"),askSide("visinu"));
	}
	
	// This method calculates the area and the perimeter of a rectangle with
	// x width and y height using x*y formula for area and 2x+2y formula for perimeter.
	// It also writes the solution on @System.out
	// x- width of the rectangle, can't be negative or zero
	// y- height of the rectangle, also can't be negative or zero
	private static void calculateAreaPerimeter (Double x, Double y) {
		Double area = x*y;
		Double perimeter = 2*x+2*y;
		System.out.format("Pravokutnik širine %s i visine %s, ima površinu %s te opseg %s.", x.toString(),y.toString(),area.toString(),perimeter.toString());
	}
	//communicates with the user asking it to enter the
	//values for a given side of the rectangle decided by the query
	private static Double askSide(String query) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.format("Unesite %s > " ,query);
			String entry = sc.next();
			try {
				Double x = parseAndCheckPositivity(entry);
				return x;
			} catch (NumberFormatException ex) {
				System.out.println(entry + " se ne može protumačiti kao broj.");
			} catch (IllegalArgumentException ex) {
				System.out.format("%s%n", ex.getMessage());
			}
		}
	}
	//parses the entered rectangle side and checks if it is positive
	//if not throws IllegalArgumentException
	private static Double parseAndCheckPositivity (String x) {
		Double number = Double.parseDouble(x);
		if(number <= 0) {
			throw new IllegalArgumentException("Unijeli ste negativnu vrijednost.");
		}
		return number;
	}
	
}
