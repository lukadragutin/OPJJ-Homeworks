package hr.fer.zemris.java.webserver;

/**
 * Sucelje koje modelira djelitelja zathjeva za obradu
 * @author Luka Dragutin
 *
 */
public interface IDispatcher {

	
	/**
	 * Salje zathjev za dohvat datoteke sa {@code urlPath}
	 * @param urlPath Put do datoteke
	 * @throws Exception Ako dode do greske prilikom citanja
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
