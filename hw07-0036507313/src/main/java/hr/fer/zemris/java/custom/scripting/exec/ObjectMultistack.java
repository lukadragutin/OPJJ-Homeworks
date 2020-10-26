package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Razred koji implementira stog koji omogucava pohranjivanje vise vrijednosti
 * pod jedan kljuc. Svaki kljuc ima poseban stog na koji se mogu pohranjivati objekti bilo kojeg tipa
 * @author Luka Dragutin
 *
 */
public class ObjectMultistack {
	
	/**
	 * Mapa kljuceva i stogova
	 */
	private HashMap<String, MultistackEntry> multistack;
	
	public ObjectMultistack() {
		multistack = new HashMap<>();
	}

	/**
	 * Na stog pod zadanim kljucem predaje vrijednost iz argumenta
	 * @param keyName Kljuc stoga
	 * @param valueWrapper Vrijednost objekta za pohranu
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		var value = multistack.get(keyName);
		if(value == null) {
			multistack.put(keyName, new MultistackEntry(valueWrapper, null));
		}
		else {
			multistack.put(keyName, new MultistackEntry(valueWrapper, value));
		}
	}
	
	/**
	 * Sa vrha stoga pod zadanim kljucem skida prvi objekt i vraca ga
	 * @param keyName Kljuc stoga
	 * @return Objekt sa vrha stoga
	 * @throws EmptyStackException Ako je stog pod zadanim kljucem prazan
	 */
	public ValueWrapper pop(String keyName) {
		if(isEmpty(keyName)) {
			throw new EmptyStackException();
		}
		var value = multistack.get(keyName);
		if(value.next != null) {
			multistack.put(keyName, value.next);
		}
		else {
			multistack.remove(keyName);
		}
		return value.value;
	}
	
	/**
	 * Dohvaca vrijednost sa vrha stoga pod zadanim kljucem,
	 * ali ju ne brise
	 * @param keyName Kljuc stoga
	 * @return Objekt sa vrha stoga
	 * @throws EmptyStackException Ako je stog pod zadanim kljucem prazan
	 */
	public ValueWrapper peek(String keyName) {
		if(isEmpty(keyName)) {
			throw new EmptyStackException();
		}
		return multistack.get(keyName).value;
	}
	
	/**
	 * Provjerava je li stog pod zadanim kljucem prazan
	 */
	public boolean isEmpty(String keyName) {
		return multistack.get(keyName) == null;
	}
	
	/**
	 * Privatna klasa koja modelira jedan cvor liste koja glumi stog u
	 * razredu {@link ObjectMultistack}
	 * @author Luka Dragutin
	 */
	private static class MultistackEntry {
		
		
		/**
		 * Referenca na sljedecu vrijednost u listi(stogu)
		 */
		private MultistackEntry next;
		
		/**
		 * Vrijednost koju pohranjuje cvor liste
		 */
		private ValueWrapper value;
		
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = Objects.requireNonNull(value);
			this.next = next;
		}
	}
}
