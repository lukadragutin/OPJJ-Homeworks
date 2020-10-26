package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Implementation of the {@link ILocalizationProvider} using the Singleton pattern.
 * Only one instance of this class can exist at a time. This instance is communicated with
 * through a bridge.
 * @see {@link FormLocalizationProvider}
 * @author Luka Dragutin
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	/**
	 * Current language
	 */
	private String language;
	
	/** Bundle that keeps all the translations for the current language */
	private ResourceBundle bundle;
	
	/**
	 * The active instance of this class
	 */
	private static LocalizationProvider instance;
	
	/**
	 * Default language
	 */
	private final static String DEFAULT_LANGUAGE = "en";

	private LocalizationProvider() {
		language = DEFAULT_LANGUAGE;
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.translation", new Locale(language));
	}

	/**
	 * If it is called for the first time, creates a new {@link LocalizationProvider}, otherwise
	 * return the reference for the already created instance {@link #instance}
	 * @return the only instance of this class
	 */
	public static LocalizationProvider getInstance() {
		if (instance == null) {
			instance = new LocalizationProvider();
		}
		return instance;
	}

	/**
	 * Sets the language
	 * @param language to be set
	 * @throws NullPointerException if the language is <code>null</code>
	 */
	public void setLanguage(String language) {
		this.language = Objects.requireNonNull(language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.translation", new Locale(language));
		fire();
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}

}
