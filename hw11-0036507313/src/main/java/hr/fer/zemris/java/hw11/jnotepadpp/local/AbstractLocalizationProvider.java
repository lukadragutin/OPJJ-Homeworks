package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic implementation of the interface {@link ILocalizationProvider} that lacks
 * the ability to provide the translated values with {@link #getString(String)}
 * and that makes it abstract
 * @author Luka Dragutin
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/** List of {@link ILocalizationListener}  */
	private List<ILocalizationListener> listeners;
	
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<>();
	}
	
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}
	
	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Signals all of the listeners that a change has happened, 
	 * generally used when the language has been changed.
	 */
	public void fire() {
		listeners.forEach(e -> e.localizationChanged());
	}
}
