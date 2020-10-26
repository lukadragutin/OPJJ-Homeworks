package hr.fer.zemris.java.hw17.jvdraw;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectListener;

/**
 * Implementacija modela korištenog u {@link JVDraw} za bilježenje elemenata
 * {@link GeometricalObject} nacrtanih u aplikaciji, te vršenje operacija nad njima
 * koje su propisane sučeljem {@link DrawingModel}
 * @see JVDraw
 * @author LukaD
 *
 */
public class DrawingModelImpl implements GeometricalObjectListener, DrawingModel {

	/**
	 * Lista nacrtanih objekata {@link GeometricalObject}
	 */
	private List<GeometricalObject> objects;
	
	/**
	 * Zastavica koja bilježi promjene nad modelom (od zadnje pohrane)
	 */
	private boolean isModified;
	
	/**
	 * Promatrači nad ovim modelom tipa {@link DrawingModelListener}
	 */
	private List<DrawingModelListener> listeners;
	
	public DrawingModelImpl() {
		listeners = new ArrayList<DrawingModelListener>();
		objects = new ArrayList<>();
	}
	
	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		object.addGeometricalListener(this);
		objects.add(object);
		int index = objects.indexOf(object);
		
		isModified = true;
		notifyAdd(index, index);
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);
		object.removeGeometricalListener(this);
		objects.remove(object);
		
		isModified = true;
		notifyRemove(index, index);
	}


	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int index = objects.indexOf(object);
		
		if(index + offset > getSize() || index + offset < 0) {
			throw new IndexOutOfBoundsException("Cannot modify because the new position is out of range");
		}
		objects.remove(object);
		objects.add(index + offset, object);
		isModified = true;
		listeners.forEach(l -> l.objectsChanged(this, index, index + offset));
	}

	@Override
	public int indexOf(GeometricalObject object) {
		return objects.indexOf(object);
	}

	@Override
	public void clear() {
		objects.forEach(o -> o.removeGeometricalListener(this));
		notifyRemove(0, getSize() - 1);
		objects.clear();
		isModified = true;
	}

	@Override
	public void clearModifiedFlag() {
		isModified = false;
	}

	@Override
	public boolean isModified() {
		return isModified;
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		int index = objects.indexOf(o);
		isModified = true;
		listeners.forEach(l -> l.objectsChanged(this, index, index));
	}

	/**
	 * Metoda koja obavještava promatrače o uklonjenim elementima sa
	 * popisa objekata na intervalu [{@code index}, {@code index2}]
	 * @param index Početak intervala
	 * @param index2 Kraj intervala
	 */
	private void notifyRemove(int index, int index2) {
		listeners.forEach(l -> l.objectsRemoved(this, index, index2));
	}
	
	/**
	 * Metoda koja obavještava promatrače o dodanim elementima na
	 * popis objekata na intervalu [{@code index}, {@code index2}]
	 * @param index Početak intervala
	 * @param index2 Kraj intervala
	 */
	private void notifyAdd(int index, int index2) {
		listeners.forEach(l -> l.objectsAdded(this, index, index2));
	}
	
}
