package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * A simple number factorial calculator
 * Asks the user to type in integers
 * in order to calculate its factorial value 
 * Accepts only integers between 3 and 20
 * @author Luka Dragutin
 * @version 1.0
 */
public class Factorial {
	
	/**
	 * The main method being called upon
	 * running the application
	 * @param args command line arguments(not used
	 * in this application)
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("Unesite broj > ");
			String input = sc.next(); //read the user input
			if(input.equals("kraj")) {  //check for the end message
				System.out.println("Doviđenja.");
				break;
			}
			int number, factorial;
			try {
				number = Integer.parseInt(input);  //parse into an integer
				factorial = calculateFactorial(number);  //calculate factorial
			} catch (NumberFormatException ex) {  //error while parsing, not an integer
				System.out.println("'" + input + "' nije cijeli broj.");
				continue;
			} catch (IllegalArgumentException ex) {
				System.out.format("%s%n",ex.getLocalizedMessage());
				//System.out.println("'" + input + "' nije broj u dozvoljenom rasponu.");
				continue;
			}
			System.out.format("%d! = %d%n", number, factorial);
		}
		sc.close();  

	}
	/**
	 * Calculates the value of x! (x factorial)
	 * @param x An integer between the values of 3 and 20.
	 * If it is not in the range throws {@link IllegalArgumentException}
	 * @throws {@link IllegalArgumentException}- if the parameter is out of range
	 * @return The value of x! (x factorial)
	 */
	public static int calculateFactorial(int x) {
		int factorial = 1;
		if(x < 3 || x > 20) {
			throw new IllegalArgumentException("'" + x + "' nije broj u dozvoljenom rasponu.");
		}
		for(int i = 2; i <= x; i++) {
			factorial *= i;
		}
		return factorial;
	}

}
