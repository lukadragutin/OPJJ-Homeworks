package searching.slagalica;

import java.util.Arrays;
import java.util.Objects;

/**
 * Pohranjuje stanje slagalice. Biljezi 
 * polozaje pojedinih brojeva i praznine na slagalici
 * @author Luka Dragutin
 *
 */
public class KonfiguracijaSlagalice {

	/**
	 * raspored brojeva u polju
	 */
	private int[] polje;

	public KonfiguracijaSlagalice(int[] polje) {
		this.polje = Objects.requireNonNull(polje);
	}

	public int[] getPolje() {
		return polje;
	}

	/**
	 * Vraca indeks praznine na slagalici, 
	 * ili -1 ako nema praznine
	 */
	public int indexOfSpace() {
		for (int i = 0; i < polje.length; i++) {
			if (polje[i] == 0) {
				return i;
			}
		}
		return -1;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(polje);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof KonfiguracijaSlagalice))
			return false;
		KonfiguracijaSlagalice other = (KonfiguracijaSlagalice) obj;
		return Arrays.equals(polje, other.polje);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < polje.length; i++) {
			if (polje[i] == 0) {
				sb.append('*');
			} else {
				sb.append(polje[i]);
			}
			if (i % 3 == 2) {
				sb.append('\n');
			} else {
				sb.append(' ');
			}
		}
		return sb.toString();
	}

	
	
}
