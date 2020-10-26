package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Razred koji dohvaca trazeni broj prvih prostih brojeva
 * @author Luka Dragutin
 *
 */
public class PrimesCollection implements Iterable<Integer> {

	/**
	 * trazeni broj prostih brojeva
	 */
	private int size;

	public PrimesCollection(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Size of collection cannot be negative!");
		}
		this.size = size;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new PrimesIterator();
	}

	private class PrimesIterator implements Iterator<Integer> {

		private int current = 0;
		private int prime = 2;

		@Override
		public boolean hasNext() {
			return current < size;
		}

		@Override
		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No more elements!");
			}
			if (prime == 2) {
				current++;
				return prime++;
			}
			while (!isPrime(prime)) {
				prime += 2;
			}
			prime += 2;
			current++;
			return prime - 2;
		}

		private boolean isPrime(int a) {
			for (int i = 2; i <= Math.sqrt(a); i++) {
				if (a % i == 0) {
					return false;
				}
			}
			return true;
		}

	}

}
