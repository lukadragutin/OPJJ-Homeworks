package hr.fer.zemris.java.hw17.jvdraw.shapes;

import hr.fer.zemris.java.hw17.jvdraw.JVDraw;

/**
 * Opisuje objekte koji se crtaju u applikaciji {@link JVDraw}.
 * Moraju biti subjekti {@link GeometricalObjectListener} promatračima i
 * implementirati metodu za {@link GeometricalObjectVisitor}
 * @author LukaD
 *
 */
public abstract class GeometricalObject {

	/**
	 * Poziva svoju metodu za obradu u objektu {@link GeometricalObjectVisitor}
	 */
	public abstract void accept(GeometricalObjectVisitor v);

	/**
	 * Stvara primjerak objekta {@link GeometricalObjectEditor} za pojedinu
	 * implementaciju {@link GeometricalObject}
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();

	/**
	 * Registrira novog promatrača
	 */
	public abstract void addGeometricalListener(GeometricalObjectListener l);

	/**
	 * Deregistrira promatrača
	 */
	public abstract void removeGeometricalListener(GeometricalObjectListener l);
}
