package hr.fer.zemris.java.hw17.jvdraw.util;

import java.awt.Color;

/**
 * Utility razred za operacije sa bojom (pretvaranje zapisa iz heksadeksadog u decimalni rgb i obratno)
 * @author LukaD
 *
 */
public class Util {

	/**
	 * Računa heksadekadsku vrijednost rgb vrijednosti boje i vraća ih kao String
	 * @param c Boja čiju vrijednost pretvaramo u heksadekadski zapis
	 * @return Heksadekadsku vrijednost predane boje <code>c</code>
	 */
	public static String getHexColor(Color c) {
		return String.format("%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
	}

	/**
	 * Iz hekadekadskog zapisa vrijednosti boje stvara primjerak 
	 * razreda {@link Color}
	 * @param hex Heksadekadska vrijednost boje
	 * @return Primjerak razreda {@link Color} nastao 
	 * iz vrijednost {@code hex}
	 * @throws IllegalArgumentException Ako je zapis krivog formata
	 * (mora biti heksadekadski i imati 6 znamenaka)
	 */
	public static Color getColorHex(String hex) {
		if (hex.length() != 6)
			throw new IllegalArgumentException("String not a valid hexadecimal number.");
		int r, b, g;
		try {
			r = (int) Long.parseLong(hex.substring(0, 2), 16);
			g = (int) Long.parseLong(hex.substring(2, 4), 16);
			b = (int) Long.parseLong(hex.substring(4, 6), 16);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Not a valid hexadecimal number.");
		}
		return new Color(r, g, b);
	}

}