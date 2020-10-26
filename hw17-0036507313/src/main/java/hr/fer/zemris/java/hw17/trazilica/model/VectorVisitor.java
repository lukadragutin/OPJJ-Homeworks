package hr.fer.zemris.java.hw17.trazilica.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@link FileVisitor} koji obilazi sve dokumente i za svaki gradi njegov
 * {@link Article} objekt u koji sprema ponavljanja pojedinih riječi i računa
 * tf-idf vektore
 * 
 * @author LukaD
 *
 */
public class VectorVisitor extends SimpleFileVisitor<Path> {

	/**
	 * Svi dokumenti iz obilaska
	 */
	private List<Article> articles;

	/**
	 * Idf vrijednosti riječi korištene za računanje tf-idf
	 */
	private HashMap<String, Double> idf;

	public VectorVisitor(HashMap<String, Double> idf) {
		super();
		articles = new ArrayList<>();
		this.idf = idf;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Article a = new Article(file.getFileName().toString());
		var tp = TextProcessor.getTextProcessor();

		var words = tp.processText(Files.readString(file, StandardCharsets.UTF_8));
		a.getVectorFromWords(words);
		a.setTFIDF(idf);

		articles.add(a);

		return FileVisitResult.CONTINUE;
	}


	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	/**
	 * Getter za {@link #articles}
	 */
	public List<Article> getArticles() {
		return articles;
	}

}
