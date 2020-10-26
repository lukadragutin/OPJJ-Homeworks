package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This interface models a simple localization provider
 * @author Luka Dragutin
 *
 */
public interface ILocalizationProvider {

	/**
	 * Adds a localization listener
	 * @param l {@link ILocalizationListener} listener
	 */
	void addLocalizationListener(ILocalizationListener l);
	
	/**
	 * Removes a localization listener
	 * @param l {@link ILocalizationListener} listener
	 */
	void removeLocalizationListener(ILocalizationListener l);

	/**
	 * Gets the current language value of the given key
	 * @param key Key that represents a value to be translated
	 * @return The current language translation of the key
	 */
	String getString(String key);

	/**
	 * Returns the current language
	 * @return the language in use
	 */
	String getCurrentLanguage();
}
