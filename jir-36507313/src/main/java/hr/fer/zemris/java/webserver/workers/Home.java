package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji delegira učitavanje i pokretanje skripte za
 * stvaranje i ispis pripadne 'homepage' html stranice koje
 * sadrži linkove na druge skripte i radnike
 * @author Luka Dragutin
 *
 */
public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		var color = context.getPersistentParameter("bgcolor");
		context.setTemporaryParameter("background", color == null ? "7F7F7F" : color);
		context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
	}
}
