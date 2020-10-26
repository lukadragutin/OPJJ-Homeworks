package hr.fer.zemris.java.hw17.trazilica.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Razred koji predstavlja jedan dokument u tf-idf modelu pretraživanja,
 * a čuva sve riječi dokumenta kao i tf i tf-idf vrijednosti istih.
 * 
 * @author LukaD
 *
 */
public class Article {

	/**
	 * Pomoćni razred za pohranu tf i tf-idf vrijednosti pojedine riječi
	 * @author LukaD
	 *
	 */
	public static class Values {

		/**
		 * Broj ponavljanja riječi u dokumentu (tf)
		 */
		int number;
		
		/**
		 * Relativna važnost riječi za dokument (tf-idf)
		 */
		double tfidf;

		public Values(int number, double tfidf) {
			this.number = number;
			this.tfidf = tfidf;
		}

		public Values() {
		}

		/**
		 * getter za {@link #number}
		 */
		public int getNumber() {
			return number;
		}

		/**
		 * setter za {@link #number}
		 */
		public void setNumber(int number) {
			this.number = number;
		}

		/**
		 * Povećava vrijednost varijable {@link #number} 
		 * (registrira još jednu pojavu riječi u dokumentu)
		 * @return ovaj primjerak razreda
		 */
		public Values addNumber() {
			number++;
			return this;
		}

		/**
		 * getter za {@link #tfidf}
		 */
		public double getTfidf() {
			return tfidf;
		}

		/**
		 * setter za {@link #tfidf}
		 */
		public void setTfidf(double tfidf) {
			this.tfidf = tfidf;
		}

	}

	/**
	 * Ime dokumenta
	 */
	private String name;
	
	/**
	 * Mapa koja predstavlja vektor tf i tf-idf vrijednosti dokumenta
	 */
	private HashMap<String, Values> vector;

	public Article(String name) {
		this.name = name;
		vector = new HashMap<>();
	}

	/**
	 * getter za {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter za {@link #name}
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Registrira pojavu riječi <code>word</code> u dokumentu,
	 * povečava njen tf vektor za 1
	 * @param word Riječ koja se registrira
	 */
	public void registerWord(String word) {
		//vector.merge(word, new Values(1, 0), (old, one) -> old.addNumber());
		var v = vector.get(word);
		if(v == null) {
			v = new Values(0, 0);
		}
		v.addNumber();
		vector.put(word, v);
	}

	/**
	 * Vraća broj pojavljivanja riječi <code>word</code> u dokumentu
	 * (tf vektor)
	 * @param word Tražena riječ
	 * @return Broj pojavljivanja tražene riječi
	 */
	public int getVector(String word) {
		var v = vector.get(word);
		if (v == null)
			return 0;
		return v.getNumber();
	}

	/**
	 * Provjerava sadrži li dokument riječ <code>word</code>
	 * @param word Tražena riječ
 	 * @return <code>true</code> ako je sadrži, <code>false</code> inače
	 */
	public boolean hasWord(String word) {
		return getVector(word) != 0;
	}

	/**
	 * Iz zadanih riječi (lista riječi dokumenta) gradi
	 * tf vektor
	 * @param text Riječi dokumenta
	 */
	public void getVectorFromWords(List<String> text) {
		for (String s : text) {
			registerWord(s);
		}
	}

	/**
	 * Vraća ukupan broj riječi u dokumentu
	 * @return Veličinu dokumenta
	 */
	public int size() {
		int i = 0;
		for (var v : vector.entrySet()) {
			i += v.getValue().getNumber();
		}
		return i;
	}

	
	/**
	 * Na osnovu tf vektora dokumenta i predanog <code>idf</code> vektora
	 * računa vrijednosti tf-idf za svaku riječ dokumenta
	 * @param idf Vektor za dokumente koje se pretražuje
	 */
	public void setTFIDF(Map<String, Double> idf) {
		for (var e : vector.entrySet()) {
			var v = idf.get(e.getKey());
			e.getValue().setTfidf((v * e.getValue().getNumber()));
		}
	}

	/**
	 * Računa i vraća veličinu tf-idf vektora dokumenta
	 */
	public double norm() {
		double sum = 0;

		for (var v : vector.entrySet()) {
			var val = v.getValue();
			sum += val.getTfidf() * val.getTfidf();
		}
		return Math.sqrt(sum);
	}

	/**
	 * Vraća tf-idf vrijednost za traženu rijec <code>word</code> 
	 */
	public double getTFIDF(String word) {
		var v = vector.get(word);
		if (v == null)
			return 0.0;
		return v.getTfidf();
	}

	/**
	 * Računa skalarni umnožak tf-idf vektora ovog dokumenta i 
	 * dokumenta {@code a}.
	 * @param a Dokument s kojim računamo skalarni umnožak
	 * @return Vraća skalarni umnožak tf-idf vektora svaju dokumenata
	 */
	public double dot(Article a) {
		double dot = 0;
		for (var v : vector.entrySet()) {
			double aValue = a.getTFIDF(v.getKey());
			dot += v.getValue().getTfidf() * aValue;
		}
		return dot;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Article))
			return false;
		Article other = (Article) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (var a : vector.entrySet()) {
			sb.append(a.getKey() + ": [" + a.getValue().getNumber() + "," + a.getValue().getTfidf() + "]\n");
		}
		return sb.toString();
	}

}
