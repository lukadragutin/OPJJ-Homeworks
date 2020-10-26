package hr.fer.zemris.java.p12;

import java.util.Objects;

/**
 * Java bean sa podacima o opciji za glasanje u anketi.
 * Sadržava njen identifikator, ime, link na reprezentativnu stranicu, identifikator
 * ankete kojoj pripada te broj glasova koji ima u anketi trenutno.
 * Naslov i link ne mogu biti null, inače se baca {@link NullPointerException}
 * @author Luka Dragutin
 *
 */
public class PollOption {

	/**
	 * Identifikator opcije
	 */
	private long id;
	
	/**Naslov/ime opcije*/
	private String optionTitle;
	
	/** Link na reprezentativnu stranicu opcije */
	private String optionLink;
	
	/** Identifikator pripadajuće ankete */
	private long pollID;
	
	/** Ukupan broj glasova opcije u anketi */
	private long votesCount;
	
	public PollOption(long id, String optionTitle, String optionLink, long pollID, long votesCount) {
		this.id = id;
		this.optionTitle = Objects.requireNonNull(optionTitle);
		this.optionLink = Objects.requireNonNull(optionLink);
		this.pollID = pollID;
		this.votesCount = votesCount;
	}
	
	public PollOption() {
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
	 * Getter za {@link #optionTitle}
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * Setter za {@link #optionTitle}
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * Getter za {@link #optionLink}
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * setter za {@link #optionLink}
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	/**
	 * Getter za {@link #pollID}
	 */
	public long getPollID() {
		return pollID;
	}

	/**
	 * Setter za {@link #pollID}
	 */
	public void setPollID(long pollID) {
		this.pollID = pollID;
	}

	/**
	 * Getter za {@link #votesCount}
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * Setter za {@link #votesCount}
	 */
	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}

	
}
