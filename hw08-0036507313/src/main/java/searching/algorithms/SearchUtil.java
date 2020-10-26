package searching.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Sadrzi algoritme za pronalazak rjesenja slagalice
 * @author Luka Dragutin
 *
 */
public class SearchUtil {

	
	/**
	 * Algoritam koji provjerava sve moguce kombinacije krecuci od
	 * pocetnog stanja dok ne dode do rjesenja, tj stanja koje zadovoljava
	 * ciljano stanje.
	 * @param s0 Pocetno stanje
	 * @param succ Funkcija za dobivanje iducih stanja
	 * @param goal Ciljano stanje
	 * @return Rjesenje slagalice
	 */
	public static <S> Node<S> bfs(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
		LinkedList<Node<S>> toExplore = new LinkedList<>();
		toExplore.addLast(new Node<S>(null, s0.get(), 0));
		while (!toExplore.isEmpty()) {
			Node<S> s = toExplore.pop();
			if (goal.test(s.getState())) {
				return s;
			}
			var children = succ.apply(s.getState());
			children.forEach((e) -> toExplore.addLast(new Node<S>(s, e.getState(), e.getCost() + s.getCost())));
		}
		return null;
	}
	
	/**
	 * Algoritam slican bfs samo sto ako dode do stanja koje se vec obradilo, 
	 * ono se preskace i tako se ubrzava rjesavanje.
	 * @param s0 Pocetno stane
	 * @param succ Funckija za dbivanje iducih stanja
	 * @param goal Ciljano stanje
	 * @return Rjesenje slagalice
	 */
	public static <S> Node<S> bfsv(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
		LinkedList<Node<S>> toExplore = new LinkedList<>();
		HashSet<S> explored = new HashSet<>();
		toExplore.addLast(new Node<S>(null, s0.get(), 0));
		explored.add(s0.get());
		while (!toExplore.isEmpty()) {
			Node<S> s = toExplore.pop();
			if (goal.test(s.getState())) {
				return s;
			}
			if(explored.contains(s.getState())) {
				continue;
			}
			var children = succ.apply(s.getState());
			explored.forEach(e -> children.remove(e));
			children.forEach((e) -> toExplore.addLast(new Node<S>(s, e.getState(), e.getCost() + s.getCost())));
			children.forEach(e -> explored.add(e.getState()));
		}
		return null;
	}
}
