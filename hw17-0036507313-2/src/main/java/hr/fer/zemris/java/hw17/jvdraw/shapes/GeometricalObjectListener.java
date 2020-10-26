package hr.fer.zemris.java.hw17.jvdraw.shapes;


/**
 * Sučelje koje modelira promatrače nad
 * razredom {@link GeometricalObject}
 * @see {@link GeometricalObject}
 * @author LukaD
 *
 */
public interface GeometricalObjectListener {

	
	/**
	 * Metoda koja implementira postupanje promatrača nakon
	 * promjene na subjektu {@link GeometricalObject} {@code o}
	 * @param o Objekt na kojem je došlo do promjene
	 */
	public void geometricalObjectChanged(GeometricalObject o);
}
