package searching.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

public class SlagalicaMain {

	public static void main(String[] args) {

		if(args.length != 1) {
			System.out.println("Argumenti kein stimung!");
			return;
		}
		
		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(getSlagalica(args[0])));

		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfs(slagalica, slagalica, slagalica);

		if (rješenje == null) {
			System.out.println("Nisam uspio pronaći rješenje.");
		} else {
			System.out.println("Imam rješenje. Broj poteza je: " + rješenje.getCost());
			List<KonfiguracijaSlagalice> lista = new ArrayList<>();
			Node<KonfiguracijaSlagalice> trenutni = rješenje;
			while (trenutni != null) {
				lista.add(trenutni.getState());
				trenutni = trenutni.getParent();
			}
			Collections.reverse(lista);
			lista.stream().forEach(k -> {
				System.out.println(k);
				System.out.println();
			});
		}
		SlagalicaViewer.display(rješenje);
	}

	private static int[] getSlagalica(String niz) {
		int[] slagalica = new int[niz.length()];
		HashSet<Integer> numbers = new HashSet<>();
		for (int i = 0; i < niz.length(); i++) {
			try {slagalica[i] = Integer.parseInt(niz.substring(i, i + 1));
				numbers.add(slagalica[i]);
			} catch (NumberFormatException e) {
				System.out.println("Krivi zapis konfiguracije!");
				System.exit(1);
			}
		}
		if(numbers.size() != 9 || numbers.contains(9)) {
			System.out.println("Krivi zapis konfiguracije!");
			System.exit(1);
		}
		return slagalica;
	}

}
