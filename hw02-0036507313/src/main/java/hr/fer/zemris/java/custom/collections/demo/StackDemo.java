package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

public class StackDemo {

	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Invalid number of arguments");
			System.exit(1);
		}
		ObjectStack stack = new ObjectStack();
		String[] entries = args[0].split(" ");
		for(String s : entries) {
			try {
				int number = Integer.parseInt(s);
				stack.push(number);
			} catch (NumberFormatException ex) {
				operation(s, stack);
			}
		}
		if(stack.size() != 1) {
			System.err.println("Invalid expression!");
			System.exit(1);
		}
		else {
			System.out.println(stack.pop());
		}
	}
	
	private static void operation(String s, ObjectStack stack) {
		try {
			int var2 =(int) stack.pop();
			int var1 =(int) stack.pop();
			switch(s) {
			case "+":
				stack.push(var1 + var2);
				break;
			case "-":
				stack.push(var1 - var2);
				break;
			case "/":
				stack.push(var1 / var2);
				break;
			case "*":
				stack.push(var1 * var2);
				break;
			case "%":
				stack.push(var1 % var2);
				break;
			default:
				System.out.println("Invalid expression!");
				System.exit(1);
			}
		} catch (EmptyStackException ex) {
			System.out.println("Invalid expression!");
			System.exit(1);
		} catch (ArithmeticException ex) {
			System.out.println("Cannot divide by zero!");
			System.exit(1);
		}
	}
}