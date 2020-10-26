package hr.fer.zemris.java.webapp2.servlets.beans;

/**
 * Java Bean containing band information.
 * @author Luka Dragutin
 *
 */
public class BandInfo {

	/** Band name */
	private String name;
	
	/** Link to a song by the band */
	private String songPath;
	
	public BandInfo(String name, String songPath) {
		this.name = name;
		this.songPath = songPath;
	}

	/**
	 * @return the name of the band
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the path to the bands song
	 */
	public String getSongPath() {
		return songPath;
	}

	/**
	 * @param songPath the path to the song to set
	 */
	public void setSongPath(String songPath) {
		this.songPath = songPath;
	}
	
	
}
