package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;

/**
 * Razred koji implementira cvor koji moze imati djecu,
 * cije reference sprema u clansku varijablu {@link ArrayIndexedCollection} children
 * @author Luka Dragutin
 *
 */
public class Node {

	/**
	 * Kolekcija referenci na djecu objekta
	 */
	private ArrayList<Node> children;
	
	/**
	 * Dodaje novo dijete u clansku varijablu children
	 * @param child Dijete koje se dodaje
	 * @throws NullPointerException ako je child null
	 */
	public void addChildNode(Node child) {
		if(children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
	}
	
	/**
	 * Vraca trenutni broj djece
	 * @return int broj djece
	 */
	public int numberOfChildren() {
		return children.size();
	}
	
	/**
	 * Vraca dijete koje se nalazi na indeksu index
	 * @param index Pozicija trazenog djeteta
	 * @return Node sa pozicije index
	 * @throws IndexOutOfBoundsException ako je index veci od broja djece
	 */
	public Node getChild(int index) {
		return children.get(index);
	}
	
		
}
