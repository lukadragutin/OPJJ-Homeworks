package hr.fer.zemris.java.webapp2.servlets;

/**
 * Java bean class that contains sinus and cosinus values of a single angle
 * @author Luka Dragutin
 */
public class Values {

	private String cos;
	private String sin;
	
	public Values(String cos, String sin) {
		this.cos = cos;
		this.sin = sin;
	}
	
	public Values() {
		cos = "1";
		sin = "0";
	}

	/**
	 * @return the cos
	 */
	public String getCos() {
		return cos;
	}

	/**
	 * @param cos the cos to set
	 */
	public void setCos(String cos) {
		this.cos = cos;
	}

	/**
	 * @return the sin
	 */
	public String getSin() {
		return sin;
	}

	/**
	 * @param sin the sin to set
	 */
	public void setSin(String sin) {
		this.sin = sin;
	}
	
	
}
