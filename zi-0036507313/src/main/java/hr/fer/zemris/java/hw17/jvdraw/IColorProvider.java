package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;

/**
 * Sučelje koje predstavlja objekte kojima se određuje 
 * boja za crtanje u {@link JVDraw} aplikaciji.
 * @author LukaD
 *
 */
public interface IColorProvider {

	/**
	 * Vraća trenutnu vrijednost boje
	 * @return Trenutni {@link Color}
	 */
	Color getCurrentColor();
	
	/**
	 * Registrira promatrača tipa {@link ColorChangeListener}
	 */
	void addColorChangeListener(ColorChangeListener l);

	/**
	 * Deregistrira promatrača tipa {@link ColorChangeListener}
	 */
	void removeColorChangeListener(ColorChangeListener l);
}
