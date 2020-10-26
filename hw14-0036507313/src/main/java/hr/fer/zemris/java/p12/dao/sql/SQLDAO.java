package hr.fer.zemris.java.p12.dao.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.p12.Poll;
import hr.fer.zemris.java.p12.PollOption;
import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova konkretna
 * implementacija očekuje da joj veza stoji na raspolaganju preko
 * {@link SQLConnectionProvider} razreda, što znači da bi netko prije no što
 * izvođenje dođe do ove točke to trebao tamo postaviti. U web-aplikacijama
 * tipično rješenje je konfigurirati jedan filter koji će presresti pozive
 * servleta i prije toga ovdje ubaciti jednu vezu iz connection-poola, a po
 * zavrsetku obrade je maknuti.
 * 
 * @author marcupic
 */
public class SQLDAO implements DAO {

	/**
	 * SQL naredba za stvaranje tablice o anketama
	 */
	private static String CREATE_POLLS = "CREATE TABLE Polls (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, title VARCHAR(150) NOT NULL, message CLOB(2048) NOT NULL)";
	
	/** SQL naredba za stvaranje tablice o opcijama za glasanje u anketama */
	private static String CREATE_POLL_OPTIONS = "CREATE TABLE PollOptions (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, optionTitle VARCHAR(100) NOT NULL, optionLink VARCHAR(150) NOT NULL, pollID BIGINT, votesCount BIGINT, FOREIGN KEY (pollID)	REFERENCES Polls(id))";

	@Override
	public List<Poll> getPollData() throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		List<Poll> polls = new ArrayList<>();
		try {
			pst = con.prepareStatement("select id, title, message from Polls order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						Poll poll = new Poll();
						poll.setId(rs.getLong(1));
						poll.setTitle(rs.getString(2));
						poll.setMessage(rs.getString(3));
						polls.add(poll);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste podataka.", ex);
		}
		return polls;
	}

	@Override
	public List<PollOption> getPollOptionsData(long pollID) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		List<PollOption> pollOptions = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"select id, optionTitle, optionLink, pollID, votesCount from PollOptions where pollID=? order by id");
			try {
				pst.setLong(1, pollID);
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollOption pollOpt = new PollOption();
						pollOpt.setId(rs.getLong(1));
						pollOpt.setOptionTitle(rs.getString(2));
						pollOpt.setOptionLink(rs.getString(3));
						pollOpt.setPollID(rs.getLong(4));
						pollOpt.setVotesCount(rs.getLong(5));
						pollOptions.add(pollOpt);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste podataka.", ex);
		}
		return pollOptions;
	}

	@Override
	public Poll getPoll(long id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		Poll poll = null;
		try {
			pst = con.prepareStatement("select id, title, message from Polls where id=?");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if (rs != null && rs.next()) {
						poll = new Poll();
						poll.setId(rs.getLong(1));
						poll.setTitle(rs.getString(2));
						poll.setMessage(rs.getString(3));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata podataka.", ex);
		}
		return poll;
	}

	@Override
	public PollOption getPollOption(long id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		PollOption pollOption = null;
		try {
			pst = con.prepareStatement(
					"select id, optionTitle, optionLink, pollID, votesCount from PollOptions where id=?");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if (rs != null && rs.next()) {
						pollOption = new PollOption();
						pollOption.setId(rs.getLong(1));
						pollOption.setOptionTitle(rs.getString(2));
						pollOption.setOptionLink(rs.getString(3));
						pollOption.setPollID(rs.getLong(4));
						pollOption.setVotesCount(rs.getLong(5));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata podataka.", ex);
		}
		return pollOption;
	}

	@Override
	public int voteFor(long id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		int row = 0;
		try {
			pst = con.prepareStatement("UPDATE PollOptions set votesCount = votesCount + 1 where id=?");
			pst.setLong(1, Long.valueOf(id));
			row = pst.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		try {
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return row;
	}

	@Override
	public void initializeDatabase() throws SQLException {
		List<Poll> polls;
		if (!checkDatabase("Polls")) {
			createDatabase(CREATE_POLLS);
			polls = setupPolls();
		}

		if (!checkDatabase("PollOptions")) {
			createDatabase(CREATE_POLL_OPTIONS);
			polls = getPollData();
			setupPollOptions(polls);
		}

		SQLConnectionProvider.setConnection(null);

	}

	/**
	 * Inicijalizira podatke o anketama
	 * 
	 * @return Listu inicijaliziranih anketa
	 */
	private List<Poll> setupPolls() {
		ArrayList<Poll> polls = new ArrayList<>();

		Poll p1 = new Poll();
		p1.setTitle("Glasanje za omiljeni bend");
		p1.setMessage("Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!");
		p1.setId(updatePollQuery(p1.getTitle(), p1.getMessage()));
		polls.add(p1);

		Poll p2 = new Poll();
		p2.setTitle("Glasanje za omiljeni film");
		p2.setMessage("Od sljedećih filmova, koji Vam je film najdraži? Kliknite na link kako biste glasali!");
		p2.setId(updatePollQuery(p2.getTitle(), p2.getMessage()));
		polls.add(p2);

		return polls;
	}

	/**
	 * Inicijalizira opcije u anketama
	 * 
	 * @param polls Lista dostupnih anketa
	 */
	private void setupPollOptions(List<Poll> polls) {
		for (Poll p : polls) {
			if (p.getTitle().contains("bend")) {
				setupBands(p.getId());
			} else if (p.getTitle().contains("film")) {
				setupFilms(p.getId());
			}
		}
	}

	/**
	 * Inicijalizira opcije u anketi o filmovima
	 * 
	 * @param id Identifikator ankete o filmovima
	 */
	private void setupFilms(long id) {
		updatePollOptionQuery("Inception", "https://www.imdb.com/title/tt1375666/", id, 0);
		updatePollOptionQuery("Shutter Island", "https://www.imdb.com/title/tt1130884/", id, 0);
		updatePollOptionQuery("Pulp Fiction", "https://www.imdb.com/title/tt0110912/", id, 0);
		updatePollOptionQuery("Django Unchained", "https://www.imdb.com/title/tt1853728/", id, 0);
		updatePollOptionQuery("Reservoir Dogs", "https://www.imdb.com/title/tt0105236/", id, 0);
		updatePollOptionQuery("The Usual Suspects", "https://www.imdb.com/title/tt0114814/", id, 0);
		updatePollOptionQuery("The Godfather", "https://www.imdb.com/title/tt0068646/", id, 0);
		updatePollOptionQuery("Predestination", "https://www.imdb.com/title/tt2397535/", id, 0);
		updatePollOptionQuery("Interstellar", "https://www.imdb.com/title/tt0816692/", id, 0);
		updatePollOptionQuery("The Room", "https://www.imdb.com/title/tt0368226/", id, 0);
	}

	/**
	 * Inicijalizira opcije u anketi o bendovima
	 * 
	 * @param id Identifikator ankete o bendovima
	 */
	private void setupBands(long id) {
		updatePollOptionQuery("Imagine Dragons", "https://www.youtube.com/watch?v=ZCSX3mM6940", id, 0);
		updatePollOptionQuery("Fall Out Boy", "https://www.youtube.com/watch?v=LBr7kECsjcQ2", id, 0);
		updatePollOptionQuery("Red Hot Chili Peppers", "https://www.youtube.com/watch?v=MCunIxyDlTg", id, 0);
		updatePollOptionQuery("Foo Fighters", "https://www.youtube.com/watch?v=ZrRbJRTRGeM", id, 0);
		updatePollOptionQuery("Nirvana", "https://www.youtube.com/watch?v=PbgKEjNBHqM", id, 0);
		updatePollOptionQuery("Gorillaz", "https://www.youtube.com/watch?v=UclCCFNG9q4", id, 0);
		updatePollOptionQuery("D Diplinzi", "https://www.youtube.com/watch?v=NS-iC6uTOi8", id, 0);
		updatePollOptionQuery("Klapa Cambi", "https://www.youtube.com/watch?v=4tr96JSMrTo", id, 0);
		updatePollOptionQuery("KUD Srce Zagore", "https://www.youtube.com/watch?v=oeHiLCizANU", id, 0);
	}

	@Override
	public int updatePollQuery(String title, String message) throws DAOException {
		Connection conn = SQLConnectionProvider.getConnection();
		int rows;
		try (PreparedStatement pst = conn.prepareStatement("INSERT INTO Polls (title, message) VALUES(?,?)",
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, title);
			pst.setString(2, message);
			rows = pst.executeUpdate();
			if (rows != 1) {
				throw new DAOException("Updating database failed. No rows affected.");
			}
			try (ResultSet key = pst.getGeneratedKeys()) {
				if (key.next()) {
					return key.getInt(1);
				} else {
					throw new DAOException("Updating database failed, no ID obtained.");
				}
			}
		} catch (SQLException ex) {
			throw new DAOException(ex);
		}
	}

	@Override
	public int updatePollOptionQuery(String optionTitle, String optionLink, long pollID, long votes)
			throws DAOException {
		Connection conn = SQLConnectionProvider.getConnection();
		int rows;

		try (PreparedStatement pst = conn.prepareStatement(
				"INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES(?,?,?,?)",
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, optionTitle);
			pst.setString(2, optionLink);
			pst.setLong(3, pollID);
			pst.setLong(4, votes);
			rows = pst.executeUpdate();
			if (rows != 1) {
				throw new DAOException("Updating database failed. No rows affected.");
			}
			try (ResultSet key = pst.getGeneratedKeys()) {
				if (key.next()) {
					return key.getInt(1);
				} else {
					throw new DAOException("Updating database failed, no ID obtained.");
				}
			}
		} catch (SQLException ex) {
			throw new DAOException(ex);
		}
	}

	/**
	 * Stvara novu tablicu, odnosno skup podataka
	 * 
	 * @param statement SQL kod za incijalizaciju tablice
	 * @throws DAOException Ako dođe do greške pri komunikaciji sa bazom podataka
	 */
	private void createDatabase(String statement) {
		Connection conn = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = conn.prepareStatement(statement)) {
			pst.executeUpdate();
		} catch (Exception ex) {
			throw new DAOException();
		}
	}

	/**
	 * Provjerava postojanje tablice sa zadanim imenom {@code tableName}
	 * @param tableName Ime tablice koja se provjerava
	 * @return <code>true</code> ako postoji tablica, <code>false</<code> inače.
	 * @throws DAOException Ako dođe do greške pri komuniciranju sa
	 * bazom podataka
	 */
	private boolean checkDatabase(String tableName) {
		Connection conn = SQLConnectionProvider.getConnection();
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, tableName.toUpperCase(), null);
			if (!rs.next()) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			throw new DAOException("Error reading data.");
		}
	}

}
