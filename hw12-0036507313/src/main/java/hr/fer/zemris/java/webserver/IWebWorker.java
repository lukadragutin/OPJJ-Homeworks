package hr.fer.zemris.java.webserver;

/**
 * Sucelje koje modelira radnike na posluzitelju
 * @author Luka Dragutin
 *
 */
public interface IWebWorker {

	/**
	 * Izvršava zadani posao komunicirajući s klijentom 
	 * preko {@code context}
	 * @param context Kontekst za obradu zahtjeva klijenta
	 * @throws Exception Ako dode do greske prilikom moguceg citanja
	 * datoteke
	 */
	public void processRequest(RequestContext context) throws Exception;
}
