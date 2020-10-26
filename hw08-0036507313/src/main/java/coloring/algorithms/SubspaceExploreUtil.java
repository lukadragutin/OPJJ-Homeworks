package coloring.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SubspaceExploreUtil {

	
	/**
	 * Algoritam bojanja koji uzima sve susjede pocetne tocke i njihove susjede(itd.), te ih
	 * ako su iste boje kao pocetni piksel sve boja, bez obzira o ponavljanju istih piksela
	 * Susjede pregledava po redu, sto je blizi piksel pocetnom, prije se obradi
	 * @param s0 Pocetni piksel
	 * @param process Bojanje piksela
	 * @param succ Funkcija za pronalazak susjednih piksela
	 * @param acceptable Provjera odgovara li boja piksela boji
	 *  pocetnog piksela
	 */
	public static <S> void bfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {
		LinkedList<S> toExplore = new LinkedList<>();
		toExplore.add(s0.get());
		while(!toExplore.isEmpty()) {
			var s = toExplore.pop();
			if(!acceptable.test(s)) {
				continue;
			}
			process.accept(s);
			succ.apply(s).forEach((e) -> toExplore.addLast(e));
		}
	}

	/**
	 * Kao i {@link SubspaceExploreUtil#bfs(Supplier, Consumer, Function, Predicate)},
	 * samo sto susjede obraduje kako stignu, neovisno o redoslijedu dolaska
	 * @param s0 Pocetni piksel
	 * @param process Funkcija bojanja
	 * @param succ Funkcija pronalaska susjeda
	 * @param acceptable Provjera odgovaranja boje piksela boji pocetnog piksela
	 */
	
	public static <S> void dfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {
		LinkedList<S> toExplore = new LinkedList<>();
		toExplore.add(s0.get());
		while(!toExplore.isEmpty()) {
			var s = toExplore.pop();
			if(!acceptable.test(s)) {
				continue;
			}
			process.accept(s);
			succ.apply(s).forEach((e) -> toExplore.addFirst(e));
		}
	}
	
	/**
	 * Kao {@link SubspaceExploreUtil#bfs(Supplier, Consumer, Function, Predicate)}, samo sto pamti
	 * vec obradene piksele i time stedi na vremenu
	 * @param s0 Pocetni piksel
	 * @param process Funkcija bojanja piksela
	 * @param succ Funkcija pronalaska susjeda piksela
	 * @param acceptable Provjera odovaranja boje piksela boji 
	 * pocetnog piksela
	 */
	public static <S> void bfsv(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {
		LinkedList<S> toExplore = new LinkedList<>();
		HashSet<S> explored = new HashSet<>();
		toExplore.add(s0.get());
		explored.add(s0.get());
		while(!toExplore.isEmpty()) {
			var s = toExplore.pop();
			if(!acceptable.test(s)) {
				continue;
			}
			process.accept(s);
			var children = succ.apply(s);
			children.removeAll(explored);
			children.forEach((e) -> toExplore.addLast(e));
			explored.addAll(children);
		}
	}

}
