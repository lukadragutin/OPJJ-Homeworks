package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;


/**
 * Model koji pohranjuje primjerke {@link GeometricalObject} nastale crtanjem
 * u {@link JVDraw}. Omogućava dodavanje, brisanje, premještanje i dohvat elemenata
 * prisutnih u modelu za crtanje. 
 * @author LukaD
 *
 */
public interface DrawingModel {

	/** Vraća broj elemenata u modelu */
	public int getSize();
	
	/**
	 * Vraća objekt na indeksu {@code index}
	 * @param index broj traženog objekta
	 * @return {@link GeometricalObject} na indeksu {@code index}
	 */
	public GeometricalObject getObject(int index);
	
	/**
	 * Dodaje novi primjerak {@link GeometricalObject} {@code object}
	 * u model.
	 * @param object Primjerak koji se dodaje u model
	 */
	public void add(GeometricalObject object);
	
	
	/**
	 * Briše {@code object} sa liste elemenata modela
	 * @param object Element koji se treba ukloniti
	 */
	public void remove(GeometricalObject object);
	
	/**
	 * Pomiče element {@code object} za {@code offset} mjesta
	 * @param object {@link GeometricalObject} koji se pomiče
	 * @param offset Broj mjesta za koji pomičemo {@code object}
	 */
	public void changeOrder(GeometricalObject object, int offset);
	
	/**
	 * Vraća redni broj primjerka {@code object} u modelu
	 * @param object Element kojem tražimo indeks
	 * @return Indeks traženog elementa, ili -1 ako ga nema
	 */
	public int indexOf(GeometricalObject object);
	
	/**
	 * Briše sve elemente sa popisa modela
	 */
	public void clear();
	
	/**
	 * Resetira zastavicu koja bilježi promjene modela. Promjene nastaju
	 * dodavanjem, brisanjem i premještanjem elemenata
	 */
	public void clearModifiedFlag();
	
	/**
	 * Vraća vrijednost zastavice isModified
	 * @returns ako ima promjena
	 * onda vraća <code>true</code>, a ako nema onda vraća <code>false</code>
	 */
	public boolean isModified();
	
	/**
	 * Registrira promatrača tipa {@link DrawingModelListener} na ovaj model
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	
	/**
	 * Deregistrira promatrača {@link DrawingModelListener} sa ovog modela
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
}
