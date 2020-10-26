package hr.fer.zemris.java.hw17.trazilica.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Singleton razred koji ima učitane stopriječi koje eliminira iz
 * dokumenata, te se koristi za obradu svakog dokumenta, tj odvaja riječi iz
 * teksovnog zapisa dokumenta i briše riječi koje nam nisu bitne (stoprijeci)
 * @author LukaD
 *
 */
public class TextProcessor {

	/**
	 * Singleton primjerak razreda
	 */
	private static TextProcessor tp;
	
	/**
	 * Stoprijeci
	 */
	private List<String> stopWords;
	
	private TextProcessor() {
		try {
			stopWords = Files.readAllLines(Paths.get("src/main/resources/hrvatski_stoprijeci.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Vraća primjerak razreda
	 * @return {@link TextProcessor} primjerak za obradu dokumenata
	 */
	public static TextProcessor getTextProcessor() {
		if(tp == null) {
			tp = new TextProcessor();
		}
		return tp;
	}
	
	/**
	 * Obrađuje tekst dokumenta tako da odvaja pojedinačne riječi i
	 * iz njih briše stoprijeke
	 * @param text Tekst dokumenta
	 * @return Listu riječi u dokumentu
	 */
	public List<String> processText(String text) {
		List<String> words = new LinkedList<String>(Arrays.asList(text.toLowerCase().split("\\P{L}+")));
		words.removeAll(stopWords);
		return words;
	}
	
}
