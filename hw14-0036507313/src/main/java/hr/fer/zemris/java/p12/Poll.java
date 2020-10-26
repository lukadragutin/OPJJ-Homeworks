package hr.fer.zemris.java.p12;

/**
 * Java bean razred koji sadržava podatke
 * o jednoj anketi. Sadrži identifikator {@code id},
 * naslov ankete {@code title} te opis/poruku ankete {@code message}
 * @author Luka Dragutin
 *
 */
public class Poll {

	/** Identifikator ankete */
	private long id;
	
	/** Naslov ankete */
	private String title;
	
	/** Poruka/opis ankete */
	private String message;
	
	public Poll() {
	}
	
	public Poll(long id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}



	/**
	 * Getter za {@link #id}
	 */
	public long getId() {
		return id;
	}
	/**
	 * Setter za {@link #id}
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * Getter za {@link #title}
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Setter za {@link #title}
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Getter za {@link #message}
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Setter za {@link #message}
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
