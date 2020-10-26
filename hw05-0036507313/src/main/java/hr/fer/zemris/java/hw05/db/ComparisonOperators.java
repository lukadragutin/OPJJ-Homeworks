package hr.fer.zemris.java.hw05.db;

/**
 * Razred koji nudi implementacije operacija usporedbe
 * između dva Stringa.
 * @author Luka Dragutin
 *
 */
public class ComparisonOperators {

	/**
	 * Ispituje je li prvi String manji od drugog 
	 */
	public static final IComparisonOperator LESS = (v1, v2) -> v1.compareTo(v2) < 0;
	
	/**Ispituje je li prvi String manji od ili jednak drugom*/
	public static final IComparisonOperator LESS_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) <= 0;
	
	/**Ispituje je li prvi String veći od drugog*/
	public static final IComparisonOperator GREATER = (v1, v2) -> v1.compareTo(v2) > 0;
	
	/**Ispituje je li prvi String veći od ili jednak drugom*/
	public static final IComparisonOperator GREATER_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) >= 0;
	
	/**Ispituje jesu li Stringovi jednaki*/
	public static final IComparisonOperator EQUALS = (v1, v2) -> v1.equals(v2);
	
	/**Ispituje jesu li Stringovi različiti*/
	public static final IComparisonOperator NOT_EQUALS = (v1, v2) -> !v1.equals(v2);
	
	/**Ispituje sadrži li prvi String strukturu zadanu preko drugog Stringa*/
	public static final IComparisonOperator LIKE = getLike();
	
	
	/**
	 * Stvara i vraća primjerak LIKE comparison operatora
	 */
	private static IComparisonOperator getLike() {
		return new IComparisonOperator() {
			
			
			@Override
			public boolean satisfied(String value1, String value2) {
				if(!value2.contains("*")) {
					return value1.startsWith(value2);
				}
				if(value2.indexOf('*') != value2.lastIndexOf('*')) {
					throw new IllegalArgumentException("Can't use more than one wildcard '*'!");
				}
				if(value2.endsWith("*")) {
					return value1.startsWith(value2.substring(0, value2.length() - 1));
				}
				if(value2.startsWith("*")) {
					return value1.endsWith(value2.substring(1));
				}
				String[] substring = value2.split("\\*");
				String regex = substring[0] + "[a-zA-Z]*" + substring[1];
				return value1.matches(regex);
			}
		};
	}
}
