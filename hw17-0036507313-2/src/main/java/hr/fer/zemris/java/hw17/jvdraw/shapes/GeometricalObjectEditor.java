package hr.fer.zemris.java.hw17.jvdraw.shapes;

import javax.swing.JPanel;

/**
 * Appstraktni razred koji modelira objekt za uređivanje
 * pojedinih {@link GeometricalObject} objekata ovisno o njihovoj implementaciji
 * Sastoji se od metoda {@link #checkEditing()} za provjeru novih parametara i 
 * {@link #acceptEditing()} za promjenu vrijednosti objekta koji se uređuje
 * @author LukaD
 *
 */
public abstract class GeometricalObjectEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556788460192395380L;
	
	/**
	 * Provjerava ispravnost vrijednosti koje su
	 * se mijenjale uređivanjem pojedinih {@link GeometricalObject}
	 */
	public abstract void checkEditing();
	
	/**
	 * Pretpostavlja da su vrijednosti koje su zadane ispravne i
	 * postavlja ih kao parametre uređivanog objekta
	 */
	public abstract void acceptEditing();
}
