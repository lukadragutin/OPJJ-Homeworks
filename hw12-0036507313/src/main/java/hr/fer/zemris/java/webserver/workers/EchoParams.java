package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji prikazuje predane parametre klijentu u obliku html tablice
 * @author Luka Dragutin
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		StringBuilder sb = new StringBuilder(
						"<html>\r\n"+
						"<head><title> Parameters </title></head>\r\n"+
						"<body>\r\n"+
						"<table>\r\n" +
					    "    <thead>\r\n" +
						"      <tr><th>Names</th><th>Values</th></tr>\r\n"+
					    "    </thead>\r\n"
						+ "    <tbody>\r\n");
		for (String s : context.getParameterNames()) {
			sb.append("      <tr><td>" + s + "</td><td>" + context.getParameter(s) + "</td></tr> \r\n");
		}
		sb.append("</tbody>\r\n" + 
				  "</table>\r\n"+
				  "</body>\r\n"+
				  "</html>\r\n\r\n");
		context.setMimeType("text/html");
		context.write(sb.toString());
	}
}
