package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;

/**
 * Sučelje koje modelira promatrača na objektu
 * {@link IColorProvider}
 * @author LukaD
 *
 */
public interface ColorChangeListener {

	/**
	 * Postupak promatrača nakon što je došlo do promjene boje
	 * @param source Objekt {@link IColorProvider} koji je poslao promjenu
	 * @param oldColor Stara vrijednost boje
	 * @param newColor Nova vrijednost boje
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
