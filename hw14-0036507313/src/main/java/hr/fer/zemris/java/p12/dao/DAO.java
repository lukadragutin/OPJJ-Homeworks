package hr.fer.zemris.java.p12.dao;

import java.sql.SQLException;
import java.util.List;

import hr.fer.zemris.java.p12.Poll;
import hr.fer.zemris.java.p12.PollOption;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	/**
	 * Dohvaca podatke o dostupnim anketama iz baze
	 * podataka u obliku liste
	 * @return Lista dostupnih anketa
	 * @throws DAOException Ako dode do greske prilikom
	 * komunikacije sa podsustavom za perzistenciju podataka 
	 */
	List<Poll> getPollData() throws DAOException;
	
	/**
	 * Dohvaca podatke o ponuđenim opcijama za glasanje
	 * koji neke ankete sa identifikatorom {@code pollID}
	 * @param pollID Identifikator ankete
	 * @return Listu opcija sa identifikatorom {@code pollID}
	 * @throws DAOException Ako dode do greške prilikom komunikacije
	 * sa podsustavom za perzistenciju podataka
	 */
	List<PollOption> getPollOptionsData(long pollID) throws DAOException;
	
	/**
	 * Dohvaća anketu spremljenu pod identifikatorom {@code id}
	 * @param id Identifikator tražene ankete
 	 * @return Podatke o traženoj anketi u primjerku razreda {@link Poll}
	 * @throws DAOException Ako dođe do greške prilikom komunikacije
	 * sa podsustavom za perzistenciju podataka
	 */
	Poll getPoll(long id) throws DAOException;
	
	/**
	 * Dohvaća podatke o opciji u anketi spremljenoj pod identifikatorom
	 * {@code id}
	 * @param id Identifikator tražene opcije
	 * @return Podatke o traženoj opciji u primjerku
	 * razreda {@link PollOption}
	 * @throws DAOException Ako dođe do greške prilikom
	 * komunikacije sa podsustavom za perzistenciju podataka
	 */
	PollOption getPollOption(long id) throws DAOException;
	
	/**
	 * Glasa za opciju pod identifikatorom {@code id}, tj povećava
	 * joj broj glasova za jedan
	 * @param id Identifikator opcije za koju se glasa
	 * @return Broj promjena među podatcima, očekivana vrijednost je 1
	 * @throws DAOException Ako dođe do greške prilikom komunikacije
	 * sa podsustavom za perzistenciju podataka
	 */
	int voteFor(long id) throws DAOException;
	
	/**
	 * Popunjava sustav za perzistenciju podataka sa podacima
	 * @throws SQLException Ako dođe do greške prilikom komunikacije
	 * sa podsustavom za perzistenciju podataka
	 */
	void initializeDatabase() throws SQLException;
	
	/**
	 * Dodaje novu anketu među podatke o anketama
	 * @param title Naslov ankete
	 * @param message Opis ankete
	 * @return Broj dodanih anketa
	 * @throws DAOException Ako dođe do greške prilikom komunikacije
	 * sa podsustavom za perzistenciju podataka
	 */
	int updatePollQuery(String title, String message) throws DAOException;
	
	/**
	 * Dodaje novu opciju među podatke o anketama
	 * @param optionTitle Ime/naslov opcije
	 * @param optionLink Link na stranicu opciju
	 * @param pollID Identifikator ankete gdje je opcija ponuđena
	 * @param votes Broj glasova
	 * @return Broj promjena u podacima
	 * @throws DAOException Ako dođe do greške prilikom
	 * komunikacije sa podsustavom za perzistenciju podataka
	 */
	int updatePollOptionQuery(String optionTitle, String optionLink, long pollID, long votes) throws DAOException;
}