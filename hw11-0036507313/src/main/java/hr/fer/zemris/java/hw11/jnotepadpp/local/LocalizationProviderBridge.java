package hr.fer.zemris.java.hw11.jnotepadpp.local;


/**
 * A bridge between a {@link ILocalizationProvider} and its listeners to prevent
 * the localization provider keeping the references to its listeners, thus stopping the
 * JVM from collecting those unwanted objects. When the localization provider is not
 * longer needed, with the method {@link #disconnect()}, the bridge unregisters hisself as
 * a listener of the Singleton {@link ILocalizationProvider} and frees the memory by becoming
 * garbage together with all his listeners.
 * @author Luka Draguitn
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * Cached language
	 */
	private String language;
	
	/**Connection to the localization provider established or not */
	private boolean connected = false;
	
	/** The {@link ILocalizationProvider} */
	private ILocalizationProvider parent;
	
	/**Listener of the localization provider  */
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			language = parent.getCurrentLanguage();
			fire();
		}
	};
	
	public LocalizationProviderBridge(ILocalizationProvider lp) {
		parent = lp;
		language = getCurrentLanguage();
	}
	
	/**
	 * Connects to the localization provider by registering himself
	 * as the providers listener
	 */
	public void connect() {
		if(connected) {
			return;
		}
		parent.addLocalizationListener(listener);
		connected = true;
		if(!language.equals(getCurrentLanguage())) {
			language = getCurrentLanguage();
			fire();
		}
	}
	
	/**
	 * Disconnects himself from the localization provider, and 
	 * with that also his listeners, by unregistering himself as
	 * a listener of the provider
	 */
	public void disconnect() {
		if(!connected) {
			return;
		}
		parent.removeLocalizationListener(listener);
		connected = false;
	}
	
	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return parent.getCurrentLanguage();
	}

	
}
