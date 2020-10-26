package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface that models a basic listener of the {@link ILocalizationProvider}
 * @author Luka Dragutin
 *
 */
public interface ILocalizationListener {

	/**
	 * Called when the language of the {@link LocalizationProvider}
	 * is changed. Normally used to update the translations with the current language.
	 */
	void localizationChanged();
}
