package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Jednostavni server koji koristi TCP protokol i HTML jezik za komunikaciju sa
 * klijentima. Implementira skripte za izradu html datoteka i razne izracune i
 * operacije. Web stranica servera je www.localhost.com, na portu 5721.
 * 
 * @author Luka Dragutin
 *
 */
public class SmartHttpServer {

	/** Zastavica za gasenje servera */
	private volatile boolean stop = false;

	/** Web adresa servera */
	@SuppressWarnings("unused")
	private String address;

	/** Naziv domene posluzitelja */
	private String domainName;

	/** Broj porta */
	private int port;

	/** Broj dretvi za obradu klijenata */
	private int workerThreads;

	/** Vrijeme prije istjecanja sesije(u sekundama) */
	private int sessionTimeout;

	/** Mapa svih mime tipova */
	private Map<String, String> mimeTypes = new HashMap<>();

	/** Opisnik glavne dretve posluzitelja */
	private ServerThread serverThread;

	/** Bazen dretvi za obradu klijenata */
	private ExecutorService threadPool;

	/** Put do root direktorija posluzitelja */
	private Path documentRoot;

	/** Mapa koja biljezi sve tipove radnika */
	private Map<String, IWebWorker> workersMap;

	/** Mapa aktivnih sesija pod sessionID-em kao kljucem */
	private Map<String, SessionMapEntry> sessions = new HashMap<>();

	/** Generator sessionID */
	private Random sessionRandom = new Random();

	/** Pretpostavljena vrijednost gasenja servera(u milisekundama) */
	@SuppressWarnings("unused")
	private final static int DEFAULT_TIMEOUT = 180000;

	/**
	 * Stvara novi web posluzitelj i postavlja ga prema konfiguracijskoj datoteci
	 * cije je ime predano kao {@code configFileName}
	 * 
	 * @param configFileName Ime konfiguracijske datoteke posluzitelja
	 * @throws RuntimeException Ako dode do greske prilikom citanja iz
	 *                          konfiguracijske datoteke
	 */
	public SmartHttpServer(String configFileName) {
		Properties properties;
		try {
			properties = readConfigFile(configFileName);
			configServer(properties);
			serverThread = new ServerThread();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Pokrece poslužitelja tako da pokrece glavnu dretvu servera, dretvu čistača i
	 * bazen dretvi
	 */
	protected synchronized void start() {
		serverThread.start();
		startCleaner();
		threadPool = Executors.newFixedThreadPool(workerThreads);
	}

	/**
	 * Pokreće dretvu čistaća koji pregledava ima li nekih aktivnih sesija kojima je
	 * vrijeme aktivnosti isteklo te ih uklanja. Provjeru vrši u intervalima
	 * jednakim {@link #sessionTimeout}
	 */
	private void startCleaner() {
		var run = new Runnable() {
			public void run() {
				while (!stop) {
					try {
						Thread.sleep(sessionTimeout * 1000);
						cleanUp();
					} catch (InterruptedException ignorable) {
						continue;
					}
				}
			}
		};
		var cleaner = new Thread(run);

		cleaner.setDaemon(true);
		cleaner.start();
	}

	/**
	 * Metoda koja prolazi aktivne sesije i izbacuje one koje su bile neaktivne vise
	 * od {@link #sessionTimeout} vrijednosti
	 */
	protected void cleanUp() {
		Date now = new Date();
		var iter = sessions.entrySet().iterator();
		while (iter.hasNext()) {
			var next = iter.next();
			if (next.getValue().validUntil - now.getTime() < 0) {
				iter.remove();
			}
		}
	}

	/**
	 * Zaustavlja server, postavlja {@link #stop} na true i gasi bazen dretvi
	 */
	protected synchronized void stop() {
		stop = true;
		threadPool.shutdown();
	}

	/**
	 * Implementacija glavne dretve posluzitelja koja u beskonacnoj petlji dohvaca
	 * zahtjeve klijenata i salje ih na obradu u bazen dretvi
	 * 
	 * @author Luka Dragutin
	 *
	 */
	protected class ServerThread extends Thread {

		@Override
		public void run() {
			ServerSocket server;
			try {
				server = new ServerSocket();
				server.bind(new InetSocketAddress((InetAddress) null, port));
				//server.setSoTimeout(DEFAULT_TIMEOUT);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			
			while (!stop) {
				try {
					Socket client = server.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				} catch (IOException | RejectedExecutionException e) {
					continue;
				}
			}
			try {
				server.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Posao obrade i komunikacije sa klijentima.
	 * 
	 * @author Luka Dragutin
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {

		/** Pristupna tocka klijenta */
		private Socket csocket;

		/** Tok podataka od klijenta */
		private PushbackInputStream istream;

		/** Tok podataka prema klijentu */
		private OutputStream ostream;

		/** Verzija protokola komunikacije */
		private String version;

		/** Metoda */
		private String method;

		/** Adresa posluzitelja */
		private String host;

		/** Mapa parametara korištenih u skriptama */
		private Map<String, String> params = new HashMap<>();

		/** Mapa privremenih parametara korištenih u skriptama */
		private Map<String, String> tempParams = new HashMap<>();

		/** Mapa trajnih parametara korištenih u skriptama */
		private Map<String, String> permParams = new HashMap<>();

		/** Odlazni kolacici */
		private List<RCCookie> outputCookies = new ArrayList<>();

		/** Identifikator sesije */
		private String SID;

		/** Opisnik zahtjeva klijenta */
		private RequestContext context = null;

		/**
		 * Stvara novi primjerak razreda pomoću pristupne točke klijenta
		 * {@link #csocket}
		 * 
		 * @param csocket Pristupna točka klijenta
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		/**
		 * Čita zahtjev predan kroz ulazni tok i na osnovu parametara iz zahtjeva šalje
		 * klijentu no što je zatražio ako je dostupno
		 */
		@Override
		public void run() {
			List<String> request;
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();
				request = readRequest();

				if (request == null || request.isEmpty()) {
					sendError(ostream, 400, "Bad request");
					csocket.close();
					return;
				}

				String firstLine = request.get(0);
				String[] args = firstLine.split(" ");
				if (args.length != 3) {
					sendError(ostream, 400, "Bad request");
					csocket.close();
					return;
				}

				method = args[0];
				String requestedPathString = args[1];
				version = args[2];
				if (!method.equalsIgnoreCase("get")
						|| !version.equalsIgnoreCase("HTTP/1.0") && !version.equalsIgnoreCase("HTTP/1.1")) {
					sendError(ostream, 400, "Bad request");
					csocket.close();
					return;
				}

				// Citanje adrese posluzitelja
				for (String line : request) {
					if (line.startsWith("Host: ")) {
						String hostValue = line.substring(line.indexOf(' ')).trim();
						if (hostValue.contains(":")) {
							hostValue = hostValue.substring(0, hostValue.indexOf(':'));
						}
						host = hostValue;
					}
				}
				if (host == null)
					host = domainName;

				// Provjera aktivnosti sesije
				checkSession(request);

				permParams = sessions.get(SID).map;

				// Parsiranje tražene adrese za prikaz
				String path = requestedPathString;
				String paramString;
				if (requestedPathString.contains("?")) {
					int index = requestedPathString.indexOf('?');
					path = requestedPathString.substring(0, index);
					paramString = requestedPathString.substring(index + 1);
					parseParameters(paramString);
				}

				internalDispatchRequest(path, true);
				csocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Provjerava je li identifikator sesije klijenta već na popisu aktivnih sesija
		 * te učitava podatke koje već ima o klijentu sa popisa, a ako nije tamo onda
		 * učitava podatke i pohranjuje ih na popis
		 * 
		 * @param request Zahtjev klijenta
		 */
		private synchronized void checkSession(List<String> request) {
			String sidCandidate = null;
			for (String s : request) {
				if (!s.startsWith("Cookie:"))
					continue;
				String[] cookies = s.substring(8).split(";");
				for (String c : cookies) {
					if (c.startsWith("sid")) {
						sidCandidate = c.substring(c.indexOf('=') + 2, c.length() - 1);
					}
				}
			}

			if (sidCandidate != null) {
				var session = sessions.get(sidCandidate);

				if (session != null && session.host.equals(host)) {
					if (session.validUntil - new Date().getTime() < 0) {
						session.validUntil = new Date().getTime() + sessionTimeout;
						SID = sidCandidate;
						return;
					} else {
						sessions.remove(sidCandidate);
					}
				}
			}
			addSession();

		}

		/**
		 * Dodaje sesiju trenutnog klijenta na popis aktivnih sesija i dodjeljuje mu
		 * novi identifikator sesije
		 */
		private void addSession() {
			SessionMapEntry sme = new SessionMapEntry();
			sme.host = host;
			sme.validUntil = new Date().getTime() + sessionTimeout;
			sme.sid = "";
			for (int i = 0; i < 20; i++) {
				sme.sid += Character.toString(sessionRandom.nextInt(32) + 65);
			}
			SID = sme.sid;
			sme.map = Collections.synchronizedMap(new HashMap<>());
			sessions.put(SID, sme);
			var cookie = new RCCookie("sid", SID, null, host, "/");
			outputCookies.add(cookie);
		}

		/**
		 * Parsira parametre predane uz adresu stranice tražene za prikaz, uglavnom
		 * korišteni za korištenje sa skriptama kao argumenti
		 * 
		 * @param paramString String sa parametrima
		 */
		private void parseParameters(String paramString) {
			String[] parameters = paramString.split("&");
			for (String p : parameters) {
				String[] mappings = p.split("=");
				if (mappings.length == 2) {
					params.put(mappings[0], mappings[1]);
				}
			}
		}

		/**
		 * Čita zahtjev klijenta
		 * 
		 * @return Zahtjev klijenta kao lista stringova
		 * @throws IOException Ako dode do greske prilikom citanja
		 */
		private List<String> readRequest() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: while (true) {
				int b = istream.read();
				if (b == -1)
					return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}
			return extractHeaders(new String(bos.toByteArray()));
		}

		/**
		 * Učitava datoteku na zahtjev klijenta ili posluzitelja iz {@code urlPath} i
		 * šalje ju klijentu.
		 * 
		 * @param urlPath    Put do tražene datoteke
		 * @param directCall Oznacava tko je trazio prikaz datoteke, <code>true</code>
		 *                   ako je klijent, <code>false</code> ako je posluzitelj
		 * @throws Exception Ako dode do greske prilikom ucitavanja datoteke
		 */
		@SuppressWarnings("deprecation")
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if (urlPath.startsWith("/private") && directCall) {
				sendError(ostream, 404, "not found");
				return;
			}
			if (context == null) {
				context = new RequestContext(ostream, params, permParams, outputCookies, tempParams, this, SID);
				context.setStatusCode(200);
				context.setStatusText("OK");
			}

			if (urlPath.startsWith("/ext/")) {
				String xxx = urlPath.substring(5);
				Package p = this.getClass().getPackage();
				String classPath = p.toString() + ".workers." + xxx;

				Class<?> referenceToClass = this.getClass().getClassLoader()
						.loadClass(classPath.replace("package ", ""));
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				iww.processRequest(context);
				return;
			}
			var worker = workersMap.get(urlPath);
			if (worker != null) {
				worker.processRequest(context);
				return;
			}

			if (urlPath.endsWith("smscr")) {
				String script = Files.readString(documentRoot.resolve(urlPath.substring(1)), StandardCharsets.UTF_8);
				SmartScriptParser scp = new SmartScriptParser(script);

				new SmartScriptEngine(scp.getDocumentNode(), context).execute();
				return;
			}

			Path requestedPath = documentRoot.resolve(urlPath.substring(1));

			String extension;
			if (!Files.exists(requestedPath) || !Files.isRegularFile(requestedPath)
					|| !Files.isReadable(requestedPath)) {
				sendError(ostream, 403, "Page not found");
				return;
			} else {
				extension = requestedPath.toString().substring(requestedPath.toString().lastIndexOf('.') + 1);
			}

			String mimeType = mimeTypes.get(extension);
			if (mimeType == null)
				mimeType = "application/octet-stream";

			context.setMimeType(mimeType);
			returnFile(requestedPath, context);
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
	}

	/**
	 * Dijeli zahtjev klijenta na redove i vraca ga kao listu redova
	 * 
	 * @param requestHeader Zahtjev klijenta
	 * @return Zahtjev klijenta kao lista redova
	 */
	private static List<String> extractHeaders(String requestHeader) {
		List<String> headers = new ArrayList<String>();
		String currentLine = null;
		for (String s : requestHeader.split("\n")) {
			if (s.isEmpty())
				break;
			char c = s.charAt(0);
			if (c == 9 || c == 32) {
				currentLine += s;
			} else {
				if (currentLine != null) {
					headers.add(currentLine);
				}
				currentLine = s;
			}
		}
		if (!currentLine.isEmpty()) {
			headers.add(currentLine);
		}
		return headers;
	}

	/**
	 * Ucitava {@code requestedFile} i salje ju klijentu pomoću {@code rc}
	 * 
	 * @param requestedFile Datoteka koja se šalje klijentu
	 * @param rc            Kontekst preko kojeg se komunicira sa klijentom
	 * @throws RuntimeException Ako dode do greške prilikom čitanja datoteke ili
	 *                          prilikom slanja iste
	 */
	private static void returnFile(Path requestedFile, RequestContext rc) {
		try (InputStream fis = new BufferedInputStream(Files.newInputStream(requestedFile))) {
			byte[] buf = new byte[1024];
			while (true) {
				int r = fis.read(buf);
				if (r < 1)
					break;
				rc.write(buf, 0, r);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	// Pomoćna metoda za slanje odgovora bez tijela...
	private static void sendError(OutputStream cos, int statusCode, String statusText) throws IOException {
		RequestContext rc = new RequestContext(cos, null, null, null);
		rc.setStatusCode(statusCode);
		rc.setStatusText(statusText);
		rc.write("");
	}

	/**
	 * Pomoćna metoda za učitavanje konfiguracijske datoteke iz imena
	 * {@code cofingFileName}
	 * 
	 * @param configFileName Ime konfiguracijske datoteke
	 * @return primjerak razreda {@link Properties} koji sadrži konfiguracije
	 * @throws IOException Ako dode do greske prilikom citanja datoteke
	 */
	private Properties readConfigFile(String configFileName) throws IOException {
		InputStream configFile;
		configFile = Files.newInputStream(Paths.get(configFileName));
		var prop = new Properties();
		prop.load(configFile);
		return prop;
	}

	/**
	 * Iz argumenta {@code properties} podešava poslužitelja
	 * 
	 * @param properties Objekt sa konfiguracijskim postavkama
	 * @throws IOException Ako dode do greske pri citanju sa diska
	 */
	@SuppressWarnings("deprecation")
	private void configServer(Properties properties) throws IOException {
		address = properties.getProperty("server.address");
		domainName = properties.getProperty("server.domainName");
		try {
			port = Integer.parseInt(properties.getProperty("server.port"));
			workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
			sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
		} catch (NumberFormatException e) {
			throw new RuntimeException("Error reading config file.");
		}
		documentRoot = Paths.get(properties.getProperty("server.documentRoot"));

		Properties mime = new Properties();
		mime.load(Files.newInputStream(Paths.get(properties.getProperty("server.mimeConfig"))));
		for (var entry : mime.entrySet()) {
			mimeTypes.put(entry.getKey().toString(), entry.getValue().toString());
		}

		workersMap = new HashMap<>();
		var workersConfig = Arrays
				.asList(Files.readString(Paths.get(properties.getProperty("server.workers"))).split("\n"));
		workersConfig.removeIf(String::isEmpty);
		workersConfig.removeIf(s -> s.startsWith("#"));
		for (String w : workersConfig) {
			String[] worker = w.split("=");
			try {
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(worker[1].trim());
				Object newObj;
				newObj = referenceToClass.newInstance();
				workersMap.put(worker[0].trim(), (IWebWorker) newObj);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Expected config file name.");
			return;
		}

		SmartHttpServer server;
		try {
			server = new SmartHttpServer(args[0]);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("Server started. Enter 'stop' to stop the server!");
		server.start();

		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		while (!line.equalsIgnoreCase("stop")) {
			System.out.println("Do not understand the input... Try again");
			line = sc.nextLine();
		}

		System.out.println("Server shutting down.");
		server.stop();
		sc.close();
	}

	/**
	 * Opisnik sesije
	 * 
	 * @author Luka Dragutin
	 *
	 */
	private static class SessionMapEntry {

		/**
		 * Identifikator sesije
		 */
		String sid;

		/**
		 * Adresa posluzitelja
		 */
		String host;

		/**
		 * 'Rok trajanja' sesije
		 */
		long validUntil;

		/**
		 * Mapa parametara sesije
		 */
		Map<String, String> map;

	}

}
