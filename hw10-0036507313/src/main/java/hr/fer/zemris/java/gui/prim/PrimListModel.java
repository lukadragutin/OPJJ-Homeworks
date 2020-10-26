package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


/**
 * Razred koji implementira model liste sa prostim brojevima
 * koji se u listu dodaju samo na poziv metode {@link #next()}.
 * Pri stvaranju liste u njoj je samo broj 1.
 * @author Luka Dragutin
 *
 */
public class PrimListModel implements ListModel<Integer> {

	/**	Lista prostih brojeva */
	private LinkedList<Integer> primes;
	
	/** Lista promatraca koji su obavijesteni nakon
	 * svake promjene u listi */
	private List<ListDataListener> listeners;

	public PrimListModel() {
		primes = new LinkedList<>();
		primes.add(1);
		listeners = new ArrayList<>();
	}

	@Override
	public int getSize() {
		return primes.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return primes.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	
	/**
	 * Stvara iduci primjerak prostog broja, odnosno
	 * prvi veci koji nije već dodan u listu, te ga
	 * dodaje na kraj liste
	 */
	public void next() {
		var lastPrime = primes.getLast().intValue();
		if (lastPrime == 1 || lastPrime == 2) {
			primes.addLast(lastPrime + 1);
		} else {
			int prime = lastPrime + 2;
			while (!isPrime(prime)) {
				prime += 2;
			}
			primes.addLast(prime);
		}
		listeners.forEach((e) -> e
				.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, primes.size(), primes.size())));
	}

	//Pomocna funkcija koja provjerava je li broj prost i 
	//ovisno o rezultatu vraca true ili false
	private boolean isPrime(int a) {
		for (int i = 2; i <= Math.sqrt(a); i++) {
			if (a % i == 0) {
				return false;
			}
		}
		return true;
	}
}
