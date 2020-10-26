package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

public class SmartScriptEngineDemo3 {

	public static void main(String[] args) {

		String documentBody;
		try {
			documentBody = Files.readString(Paths.get("webroot/scripts/brojPoziva.smscr"));
		} catch (IOException e) {
			System.out.println("Couldn't read file.");
			return;
		}
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();// create engine and execute it
		persistentParameters.put("brojPoziva", "3");

		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);
		
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
	}

}
