package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji postavlja boju pozadine 'home page' stranice na
 * boju predanu kao argument ako je valjana i o tome obavjestava korisnika,
 * a ne mijenja nista ako nije valjana
 * @author Luka Dragutin
 *
 */
public class BgColorWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		boolean updated = false;
		var color = context.getParameter("bgcolor");
		if(color != null) {
			try {
				Color.getColor(color);
				context.setPersistentParameter("bgcolor", color);
				updated = true;
			} catch (NumberFormatException e) {
			}
		}
		
		context.write("<html>\r\n"+
					  "  <body>\r\n"+
					  "    <h1>Message</h1>\r\n\r\n"+
					  "    <a href=\"/index2.html\">HomePage</a>\r\n"+
					  "    Color is "+(updated ? "" : "not ")+"updated.\r\n"+
					  "  </body>\r\n"+
					  "</html>\r\n"+
					  "\r\n");
	}
}
