package searching.slagalica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Transition;


/**
 * Implementira operacije nad slagalicom pri trazenju rjesenja
 * @author Luka Draugutin
 *
 */
public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
		Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

	/**
	 * Stanje slagalice
	 */
	private KonfiguracijaSlagalice konf;

	public Slagalica(KonfiguracijaSlagalice konf) {
		this.konf = Objects.requireNonNull(konf);
	}

	@Override
	public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice t) {
		ArrayList<Transition<KonfiguracijaSlagalice>> list = new ArrayList<>();
		int space = t.indexOfSpace();
		if (space >= 3) {
			moveSpace(-3, list, t);
		}
		if (space < 6) {
			moveSpace(3, list, t);
		}
		if (space % 3 != 0) {
			moveSpace(-1, list, t);
		}
		if (space % 3 != 2) {
			moveSpace(1, list, t);
		}
		return list;
	}

	/**
	 * Vrši zamjenu praznine slagalice u jednom od mogucih smjerova
	 * @param move Broj pomaka praznine
	 * @param list Lista buducih stanja
	 * @param t Trenutno stanje
	 */
	private void moveSpace(int move, ArrayList<Transition<KonfiguracijaSlagalice>> list, KonfiguracijaSlagalice t) {
		var t2 = t.getPolje().clone();
		int swapped = 0;
		int space = t.indexOfSpace();
		if (move > 0) {
			for (int i = t2.length; i >= 0; i--) {
				if (i == space + move) {
					swapped = t2[i];
					t2[i] = 0;
				}
				if (i == space) {
					t2[i] = swapped;
				}
			}
		} else {
			for (int i = 0; i < t2.length; i++) {
				if (i == space + move) {
					swapped = t2[i];
					t2[i] = 0;
				}
				if (i == space) {
					t2[i] = swapped;
				}
			}
		}
		list.add(new Transition<KonfiguracijaSlagalice>(new KonfiguracijaSlagalice(t2), 1));
	}

	@Override
	public KonfiguracijaSlagalice get() {
		return konf;
	}

	@Override
	public boolean test(KonfiguracijaSlagalice t) {
		return Arrays.equals(t.getPolje(), new int[] {1,2,3,4,5,6,7,8,0});
	}

}
