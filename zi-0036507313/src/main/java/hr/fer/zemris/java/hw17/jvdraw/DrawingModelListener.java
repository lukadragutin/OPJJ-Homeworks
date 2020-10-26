package hr.fer.zemris.java.hw17.jvdraw;

/**
 * Sučelje koje opisuje promatrače nad {@link DrawingModel}
 * @author LukaD
 *
 */
public interface DrawingModelListener {

	/**
	 * Postupak nakon što su u {@link DrawingModel}-u dodani objekti
	 * @param source {@link DrawingModel} u kojem su dodani objekti
	 * @param index0 Početak intervala promjene
	 * @param index1 Kraj intervala promjene
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Postupak nakon što su u {@link DrawingModel}-u uklonjeni objekti
	 * @param source {@link DrawingModel} u kojem su uklonjeni objekti
	 * @param index0 Početak intervala promjene
	 * @param index1 Kraj intervala promjene
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);
	
	/**
	 * Postupak nakon što su u {@link DrawingModel}-u promjenjeni objekti
	 * @param source {@link DrawingModel} u kojem su objekti promjenjeni
	 * @param index0 Početak intervala promjene
	 * @param index1 Kraj intervala promjene
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
