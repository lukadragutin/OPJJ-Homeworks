package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Kontekst komunikacije klijenta i posluživača koji
 * sadrži sve potrebne informacije
 * @author Luka Dragutin
 *
 */
public class RequestContext {

	/**Izlazni tok (prema klijentu) */
	private OutputStream outputStream;
	
	/**Tip charseta za prikaz teksta*/
	private Charset charset;
	
	/**Zapis charseta*/
	private String encoding = "UTF-8";
	
	/**Status kod*/
	private int statusCode = 200;
	
	/**Status tekst*/
	private String statusText = "OK";
	
	/**Tip zapisa prijenosa podataka prema klijentu*/
	private String mimeType = "text/html";
	
	/**Velicina tijela*/
	private Long contentLength = null;
	
	/**Parametri*/
	private Map<String, String> parameters;
	
	/**Privremeni parametri*/
	private Map<String, String> temporaryParameters;
	
	/**Trajni parametri*/
	private Map<String, String> persistentParameters;
	
	/**Kolacici*/
	private List<RCCookie> outputCookies;
	
	/**Zastavica koja oznacava je li zaglavlje vec generirano*/
	private boolean headerGenerated = false;
	
	/**Dijelitelj zahtjeva*/
	private IDispatcher dispatcher;
	
	/**Identifikator sesije*/
	private String sid;

	
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistentParameters, outputCookies, null, null, null);
		}
	
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,Map<String, String>  temporaryParameters, IDispatcher dispatcher, String sid) {
		this.outputStream = Objects.requireNonNull(outputStream);
		this.parameters = parameters == null ? new HashMap<String, String>() : parameters;
		this.persistentParameters = persistentParameters == null ? new HashMap<String, String>() : persistentParameters;
		this.outputCookies = outputCookies == null ? new ArrayList<RCCookie>() : outputCookies;
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
		this.sid = sid;
	}
	
	
	/**
	 * @return vraca {@link #dispatcher}
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * @param encoding tip kodiranja za postaviti
	 * @throws RuntimeException Ako je zaglavlje vec postavljeno
	 */
	public void setEncoding(String encoding) {
		if(headerGenerated) {
			throw new RuntimeException("Header already set!");
		}
		this.encoding = Objects.requireNonNull(encoding);
	}




	/**
	 * statusCode setter
	 * @throws RuntimeException Ako je zaglavlje vec postavljeno
	 */
	public void setStatusCode(int statusCode) {
		if(headerGenerated) {
			throw new RuntimeException("Header already set!");
		}
		this.statusCode = statusCode;
	}




	/**
	 * statusText setter
	 * @throws RuntimeException Ako je zaglavlje već postavljeno
	 */
	public void setStatusText(String statusText) {
		if(headerGenerated) {
			throw new RuntimeException("Header already set!");
		}
		this.statusText = Objects.requireNonNull(statusText);
	}



	/**
	 * mimeType setter
	 * @throws RuntimeException Ako je zaglavlje već postavljeno
	 * @throws NullPointerException Ako je argument <code>null</code>
	 */
	public void setMimeType(String mimeType) {
		if(headerGenerated) {
			throw new RuntimeException("Header already set!");
		}
		this.mimeType = Objects.requireNonNull(mimeType);
	}

	

	/**
	 * contentLength setter
	 * @throws RuntimeException Ako je zaglavlje postavljeno
	 */
	public void setContentLength(Long contentLength) {
		if(headerGenerated) {
			throw new RuntimeException("Header already set!");
		}
		this.contentLength = contentLength;
	}


	/**
	 * Dohvaca parametar pod kljucem {@code name}
	 * @param name Kljuc za dohvat parametra
	 * @return vrijednost parametra spremljena pod {@code name}
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Dohvaca read-only skup parametara
	 * @return Skup parametara
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Dohvaca trajni parametar pod kljucem {@code name}
	 * @param name Kljuc za dohvat trajnog parametra
	 * @return vrijednost trajnog parametra spremljena pod {@code name}
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Vraća read-only skup trajnih parametara
	 * @return Skup trajnih parametara
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Dodaje novi trajni parametar pod kljucem {@code name} sa vrijednosti {@code value}
	 * @param name Kljuc/naziv parametra
	 * @param value Vrijednost parametra
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Uklanja trajni parametar pod kljucem {@code name}
	 * @param name Naziv parametra koji se uklanja
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Dohvaća privremeni parametar pod kljucem {@code name}
	 * @param name Ključ/naziv privremenog parametra
	 * @return Vrijednost privremenog parametra pod kljucem {@code name}
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters == null ? null : temporaryParameters.get(name);
	}

	/**
	 * Vraca skup privremenih parametara
	 * @return Skup privremenih parametara
	 */
	public Set<String> getTemporaryParameterNames() {
		return temporaryParameters == null ? null : Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Getter za {@link #sid}
	 * 
	 */
	public String getSessionID() {
		return sid;
	}

	/**
	 * Dodaje novi privremeni parametar pod kljucem {@code name} sa vrijednosti {@code value}
	 * @param name Kljuc/naziv privremenog parametra
	 * @param value Vrijednost parametra
	 * @throws NullPointerException Ako je naziv parametra <code>null</code>
	 */
	public void setTemporaryParameter(String name, String value) {
		if(temporaryParameters == null) {
			temporaryParameters = new HashMap<String, String>();
		}
		temporaryParameters.put(Objects.requireNonNull(name), value);
	}
	
	/**
	 * Uklanja privremeni parametar pod kljucem {@code name}
	 * @param name Kljuc/naziv parametra koji se uklanja
	 */
	public void removeTemporaryParameter(String name) {
		if(temporaryParameters != null) {
			temporaryParameters.remove(name);
		}
	}
	
	/**
	 * Ispisuje na izlazni tok polje bajtova {@code data}
	 * @param data Polje bajtova za ispis
	 * @return Primjerak konteksta
	 * @throws IOException Ako dode do greske pri ispisu
	 */
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}
	
	/**
	 * Ispisuje na izlazni tok predani tekst {@code text}
	 * @param text Tekst za ispis
	 * @return Primjerak konteksta
	 * @throws IOException Ako dode do greske pri ispisu
	 */
	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) generateHeader();

		byte[] data = text.getBytes(charset);
		return write(data, 0, data.length);
	}
	
	/**
	 * Ispisuje iz predanog polja bajtova od indeksa {@code offset} idućih {@code len} vrijednosti
	 * @param data Polje bajtova za ispis
	 * @param offset Indeks prvog bajta za ispis
	 * @param len Kolicina bajtova za ispis
	 * @return Primjerak konteksta
	 * @throws IOException Ako dode do greske pri ispisu
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if(!headerGenerated) generateHeader();
		
		outputStream.write(data, offset, len);
		return this;
	}
	
	/**
	 * Na osnovu parametara {@link #sid}, {@link #statusCode}, {@link #statusText}, 
	 * {@link #contentLength} i {@link #outputCookies} stvara zaglavlje podataka koje salje
	 * @throws IOException Ako dode do greske prilikom ispisa zaglavlja
	 */
	private void generateHeader() throws IOException {
		charset = Charset.forName(encoding);
		
		StringBuilder sb = new StringBuilder(
				"HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
				"Content-Type: " + mimeType +
							(mimeType.startsWith("text/") ? "; charset=" + encoding :"") +"\r\n");
		if(contentLength != null) {
			sb.append("Content-Length:" + contentLength.longValue() + "\r\n");
		}
		
		if(!outputCookies.isEmpty()) {
			for(RCCookie rc : outputCookies) {
				sb.append("Set-Cookie: " + rc.name + "=" + "\"" + rc.value + "\"");
				
				if(rc.domain != null) {
					sb.append("; Domain=" + rc.domain);
				}
				
				if(rc.path != null) {
					sb.append("; Path=" + rc.path);
				}
				
				if(rc.maxAge != null) {
					sb.append("; Max-Age=" + rc.maxAge.intValue());
				}
				
				if(rc.httpOnly) {
					sb.append("; HttpOnly");
				}
				
				sb.append("\r\n");
			}
		}

		
		sb.append("\r\n");
		outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
		headerGenerated = true;
	}
	
	/**
	 * Cuva bitne podatke za buduce koristenje
	 * @author Luka Dragutin
	 *
	 */
	public static class RCCookie {

		/**
		 * Naziv vrijednost
		 */
		private String name;
		
		/**Spremljena vrijednost */
		private String value;
		
		/**Domena stranice za koju se cuva vrijednost */
		private String domain;
		
		/**Put na stranici za koju se cuva vrijednost*/
		private String path;
		
		/**Valjanost zapisa (u sekundama)*/
		private Integer maxAge;
		
		/**Zastavica za oznaku ako je pristup kolacicu dozvoljen 
		 * samo korisnicima http protokola*/
		private boolean httpOnly = true;

		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * getter za {@link #name}
		 */
		public String getName() {
			return name;
		}

		/**
		 * getter za {@link #value}
		 */
		public String getValue() {
			return value;
		}

		/**
		 * getter za {@link #domain}
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * getter za {@link #path}
		 */
		public String getPath() {
			return path;
		}

		/**
		 * getter za {@link #maxAge}e
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
		
		/**
		 * setter za {@link #httpOnly}
		 */
		public void setHttpOnly(boolean httpOnly) {
			this.httpOnly = httpOnly;
		}
		
		/**
		 * getter za {@link #getHttpOnly()}
		 */
		public boolean getHttpOnly() {
			return httpOnly;
		}

	}

	/**
	 * Dodaje kolacic {@code rcCookie} na listu kolacica
	 * @param rcCookie KOlacic koji se dodaje na listu kolacica
	 */
	public void addRCCookie(RCCookie rcCookie) {
		outputCookies.add(rcCookie);
	}
}
