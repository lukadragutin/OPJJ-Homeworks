package hr.fer.zemris.java.hw17.trazilica.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link FileVisitor} koji obilazi sve dokumente i gradi skup riječi koje
 * se pojavljuju u dokumentima (vokabular) ne pamteći stopriječi. Također pamti 
 * broj dokumenata u kojima se koja rijč iz vokabulara pojavljivala (za izgradnju idf vektora)
 * @author LukaD
 *
 */
public class VocabularVisitor extends SimpleFileVisitor<java.nio.file.Path> {

	/**
	 * Riječi sa brojem dokumenata u kojima se pojavljuju
	 */
	private HashMap<String, Integer> words;
	
	/**
	 * Skup jedinstvenih riječi (vokabular)
	 */
	private Set<String> vocabulary;
	
	/**
	 * Broj dokumenata koje se obišlo
	 */
	private int cntr;
	
	public VocabularVisitor() {
		super();
		vocabulary = new HashSet<>();
		words = new HashMap<String, Integer>();
		cntr = 0;
	}

	@Override
	public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attrs) throws IOException {
		
		String text = Files.readString(file, StandardCharsets.UTF_8);
		var procText = TextProcessor.getTextProcessor().processText(text);
		
		Set<String> setText = new HashSet<>(procText);
		
		for(String t : setText) {
			vocabulary.add(t);
			Integer i = words.get(t);
			if(i == null) {
				i = Integer.valueOf(0);
			}
			words.put(t, Integer.valueOf(i + 1));
		}
		
		cntr++;
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(java.nio.file.Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}
	
		
	/**
	 * Getter za {@link #words}
	 */
	public HashMap<String, Integer> getWords() {
		return words;
	}
	
	/**
	 * Getter za {@link #vocabulary}
	 */
	public List<String> getVocabulary() {
		ArrayList<String> list = new ArrayList<>(vocabulary);
		list.sort(null);
		return list;		
	}

	/** Getter za {@link #cntr} */
	public int getCntr() {
		return cntr;
	}
}
