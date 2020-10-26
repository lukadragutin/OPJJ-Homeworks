package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * An application that allows the user to 
 * enter integer values and stores them
 * in the binary tree set, which are in the end sorted
 * in ascending and descending order.
 * @author Luka Dragutin
 * @version 1.0
 */
public class UniqueNumbers {
	/**
	 * Method that adds any integer value to the binary tree if
	 * it is not already part of the tree
	 * @param root The root of the binary tree to which the value is added
	 * @param value The value being added to the tree, can only be an integer
	 * @return New TreeNode with the added value in it, or the unchanged tree
	 * if the value has already been in the tree beforehand
	 */
	public static TreeNode addNode(TreeNode root, int value) {
		if(root == null) { //if the tree is empty we create the first node
			root = new TreeNode();
			root.value = value;
			root.left = null;
			root.right = null;
			return root;
		}
		if(root.value > value) {
			root.left = addNode(root.left,value); //recursion
			return root;
			}	
		else {
			root.right = addNode(root.right,value); //recursion
			return root;
		}
	}
	/**
	 * Calculates the size of the binary tree using recursion
	 * @param The root of the measured binary tree
	 * @return Size of the tree (integer)
	 */
	public static int treeSize(TreeNode root) {
		if(root == null) {
			return 0;
		}
		return 1 + treeSize(root.left) + treeSize(root.right);//returns the sum of the
	}																	//current node and its children
	
	public static boolean containsValue (TreeNode root, int value) {
		if(root == null) {
			return false;
		}
		if(root.value == value) {
			return true;
		}
		if(root.value > value) {
			return root.left == null ? false : containsValue(root.left, value);
		}
		else {
			return root.right == null ? false : containsValue(root.right, value);
		}
	}
	
	/**
	 * Prints a binary tree in either descending or ascending order
	 * @param root Root of the binary tree to be printed
	 * @param Descending If true prints the tree in descending order,
	 * if false prints it in ascending order
	 */
	public static void printTreeNode(TreeNode root, Boolean Descending) {
		if(!Descending) {
			System.out.format("Ispis od najmanjeg: %s%n", toString(root, Descending));
		}
		else {
			System.out.format("Ispis od najvećeg: %s%n", toString(root, Descending));
		}
	}
	/**
	 * A helping method for printing the binary tree using recursion.
	 * Gives the tree's values in either ascending or descending order
	 * @param root Root of the binary tree
	 * @param Ascending Decides the order of the values (if true ascending, if false descending)
	 * @return String of the trees values
	 */
	public static String toString(TreeNode root,Boolean Ascending) {
		if(root == null) {
			return "";
		}
		if(!Ascending) {
			return new String(toString(root.left, Ascending) + " " + root.value + " " + toString(root.right, Ascending)); 
		}
		else {
			return new String(toString(root.right, Ascending) + " " + root.value + " " + toString(root.left, Ascending));
		}
	}
	/**
	 * Communicates with the user prompting him to enter 
	 * values for the binary tree.
	 * Doesn't accept duplicate values or non-integer values
	 * @param args not used
	 */
	public static void main(String[] args) {
		TreeNode root = null;
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.format("Unesite broj > ");
			String entry = sc.next();
			if(entry.equals("kraj")) {
				break;
			}
			try {
				int value = Integer.parseInt(entry);
				if(containsValue(root, value)) {
					System.out.println("Broj već postoji. Preskačem.");
					continue;
				}
				root = addNode(root, value);
				System.out.println("Dodano");
			} catch (NumberFormatException ex) {
				System.out.format("'%s' nije cijeli broj.%n", entry);
			}
		}
		sc.close();
		printTreeNode(root, false);
		printTreeNode(root, true);
	}
	/**
	 * Class representing the structure of a node in a binary tree.
	 * It has an integer value and references to its left and right 
	 * ascendant nodes.  
	 *
	 */
	public static class TreeNode {
		TreeNode left;
		TreeNode right;
		int value;
	}
		
		//Checks if the binary tree already contains the wanted value
		//Returns true if it does, and false if it doesn't
		
		//Getters and setters for the TreeNode variables
		
}
