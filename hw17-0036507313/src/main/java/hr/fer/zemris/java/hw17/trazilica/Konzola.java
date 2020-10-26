package hr.fer.zemris.java.hw17.trazilica;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

import hr.fer.zemris.java.hw17.trazilica.model.Article;
import hr.fer.zemris.java.hw17.trazilica.model.VectorVisitor;
import hr.fer.zemris.java.hw17.trazilica.model.VocabularVisitor;


/**
 * Program komandne linije koji omogućava korisniku pretraživanje 
 * dokumenata iz direktorija predanog kao argument. Korisnik unosi zahtjev
 * koji se sadrži od ključnih riječi pomoću kojih se dokumenti filtriraju 
 * koristeći TF-IDF vektore za usporedbu dokumenata i ispisuje dokumente koji su
 * najrelevantniji za unesene ključne riječi. Opisana naredba je 'query': <br>
 * 
 * "query hajduk nogomet". <br>
 * Također pruža sljedeće naredbe :
 * <li><b>results</b>: Ponovno ispisuje rezultate zadnjeg pretraživanja.</li>
 * <li><b>type [x]</b>: Ispisuje tekst dokumenta pod rednim brojem x u rezultatima.</li>
 * <li><b>repeat [s]</b>: Ispisuje broj ponavljanja riječi s u dokumentima.</li>
 * <li><b>exit</b>: Gasi aplikaciju.</li>
 * @author LukaD
 *
 */
public class Konzola {

	
	/**
	 * Riječi vokabulara koje kao vrijednost imaju pridužen broj 
	 * različitih dokumenata u kojima se pojavljuju
	 */
	public static HashMap<String, Integer> words;
	
	/**
	 * Sve različite riječi iz dokumenata (vokabular).
	 */
	public static List<String> vocabulary;
	
	/**
	 * Lista svih dokumenata te njihovih svojstava (pripadni tfidf vektori)
	 */
	static List<Article> articles;
	
	/**
	 * IDF vektori riječi u vokabularu ( idf od rijeci 
	 * w = log(broj dokumenata u kolekciji/ broj dokumenata u kojima se pojavljuje riječ s)) 
	 */
	static HashMap<String, Double> idf;
	
	/**
	 * Lista dokumenata koji su rezultati pretrage po ključnim riječima poredanih po 
	 * relevantnosti odnosno sličnosti.
	 */
	static NavigableMap<Double, Article> similar;
	
	/** Put do direktorija sa datotekama */
	static Path files;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Krivi broj argumenata. Očekivao sam put do direktorija sa vokabularom. Izlazim...");
			return;
		}

		files = Paths.get(args[0]);

		loadVocabulary();
		loadVectors();

		System.out.println("Veličina riječnika je " + vocabulary.size() + " riječi.");

		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("Enter command > ");
			String line = sc.nextLine().trim();
			if (line.isEmpty()) {
				System.out.println("Prazna naredba.");
				continue;
			}
			String[] command = line.split("\\s+", 2);

			switch (command[0]) {
			case "query":
				if (command.length < 2) {
					System.out.println("Nisu uneseni argumenti.");
					continue;
				} else {
					query(command[1]);
				}
				continue;
			case "type":
				if (command.length != 2) {
					System.out.println("Nije unesen argument.");
					continue;
				}
				type(command[1]);
				continue;
			case "results":
				getResults();
				continue;
			case "exit":
				sc.close();
				return;
			case "repeat":
				System.out.println(words.get(command[1]));
				continue;
			default:
				System.out.println("Nepoznata naredba.");
			}
		}
	}

	
	/**
	 * Metoda koja ispisuje traženi dokument čije je ime predano kao
	 * argument metode. Izvršava naredbu type [] 
	 * @see Konzola
	 * @param s Naziv datoteke koja se učitava u komandnu liniju
	 */
	private static void type(String s) {
		int index;
		try {
			index = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			System.out.println(s + " se ne moze protumaciti kao broj");
			return;
		}

		if (index >= similar.size()) {
			System.out.println("Indeks izvan dosega");
			return;
		}
		Article[] similarArticles = similar.values().toArray(new Article[similar.size()]);
		Article queried = similarArticles[index];

		Path path = files.resolve(queried.getName());
		System.out.println("Dokument: " + path.toString());

		try {
			Files.readAllLines(path, StandardCharsets.UTF_8).forEach(System.out::println);
		} catch (IOException e) {
			System.out.println("Greška pri čitanju datoteke.");
		}
	}

	
	/**
	 * Metoda koja vrši pretraživanje po dokumentima koristeći njihove
	 * tf-idf vektore i ispisuje one koji su najsličniji za pojmove u tražilici.
	 * Ispisuje 10 najsličnijih rezultata u formatu: <br>
	 * [redni broj] (sličnost) put do datoteke
	 * @param args String zapis unesenog upita
	 */
	private static void query(String args) {
		String[] split = args.split("\\s+");
		List<String> tested = new ArrayList<>();

		for (String s : split) {
			if (vocabulary.contains(s)) {
				tested.add(s);
			}
		}

		if (tested.isEmpty()) {
			System.out.println("No similarities.");
			return;
		}
		
		printQuery(tested);

		Article test = new Article("test");
		test.getVectorFromWords(tested);
		test.setTFIDF(idf);

		System.out.println(test);

		TreeMap<Double, Article> tempMap = new TreeMap<>();
		for (Article a : articles) {
			double sim = getSimilarity(a, test);
			if (sim != 0) {
				tempMap.put(sim, a);
			}
		}

		similar = tempMap.descendingMap();

		System.out.println("Najboljih " + (similar.size() > 10 ? 10 : similar.size()) + " rezultata.");

		getResults();
		
	}

	/**
	 * Pomoćna metoda koja ispisuje prihvaćene riječi upita, odnosno
	 * one koje se barem jednom pojavljuju u dokumentima koji su na raspolaganju
	 * @param tested Lista riječi za koje se izvršava pretraga dokumenata
	 */
	private static void printQuery(List<String> tested) {
		System.out.print("Query is: [");
		int i = 0;
		for (String s : tested) {
			if (i == 0) {
				System.out.print(s);
			} else {
				System.out.print(", " + s);
			}
			i++;
		}
		System.out.println("]\n");
	}

	/**
	 * Vraća sličnost dva dokumenta kao vrijednost kuta 
	 * njihovih vektora sa tf-idf vrijednostima riječi za pojedini dokument pomoću formule: <br>
	 * <b> skalarni umnožak vektora / veličina 1. * veličina 2. vektora </b>
	 * @param a Prvi dokument u usporedbi 
	 * @param b Drugi dokument u usporedbi
	 * @return Vrijednost kuta između vektora dokumenata, tj. sličnost
	 */
	private static double getSimilarity(Article a, Article b) {
		return a.dot(b) / (a.norm() * b.norm());
	}

	/**
	 * Ispisuje rezultate upita
	 */
	private static void getResults() {
		int i = 0;
		for (var e : similar.entrySet()) {
			if (i >= 10)
				break;
			System.out.printf("[%d](%.4f) %s\n", i, e.getKey(),
					files.resolve(Paths.get(e.getValue().getName())).toString());
			i++;
		}

	}

	/**
	 * Čita dokumente i gradi njihove tf-idf vektore
	 */
	private static void loadVectors() {
		VectorVisitor vv = new VectorVisitor(idf);
		try {
			Files.walkFileTree(files, vv);
		} catch (IOException e) {
			System.out.println("Error reading files.");
			System.exit(1);
		}

		articles = vv.getArticles();
	}

	/**
	 * Čita dokumente i stvara vokabular te idf vektore
	 */
	private static void loadVocabulary() {
		VocabularVisitor vv = new VocabularVisitor();
		try {
			Files.walkFileTree(files, vv);
		} catch (IOException e) {
			e.printStackTrace();
		}

		words = vv.getWords();
		vocabulary = vv.getVocabulary();
		int cntr = vv.getCntr();

		idf = new HashMap<>();

		calculateIdf(cntr);
	}

	/**
	 * Računa idf vrijednosti riječi vokabulara
	 * @param cntr Broj dokumenata pročitanih
	 */
	private static void calculateIdf(int cntr) {
		for (var e : words.entrySet()) {
			double eIdf = Math.log((double) cntr / e.getValue());
			idf.put(e.getKey(), eIdf);
		}
	}

}
