package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * An abstract implementation of the abstract class {@link AbstractAction} 
 * that makes creating actions with localization settings much easier.
 * Every new object is added to the listeners of a {@link FormLocalizationProvider}
 * to get language updates, and automatically updates its localization values
 * @author Luka Dragutin
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	/** the localization provider */
	private ILocalizationProvider prov;
	
	/**Localization key of the action name  */
	private String keyName;
	
	/** Localization key of the action description  */
	private String keyDescription;
	
	/** Anonymous listener of the localization provider that
	 * updated the action name and description when called
	 */
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			updateAction();
		}
	};
	
	/**
	 * Creates an localized action using localization keys and a
	 * localization provider
	 * @param keyName Key for the action name localization
	 * @param keyDescription Key for the action description localization
	 * @param lp localization provider
	 * @throws NullPointerException if the arguments are <code>null</code>
	 */
	public LocalizableAction(String keyName, String keyDescription, ILocalizationProvider lp) {
		this.keyName = Objects.requireNonNull(keyName);
		this.keyDescription = Objects.requireNonNull(keyDescription);
		this.prov = Objects.requireNonNull(lp);
		updateAction();
		prov.addLocalizationListener(listener);
	}
	
	/**
	 * Updates the localized values
	 */
	private void updateAction() {
		putValue(Action.NAME, prov.getString(keyName));
		putValue(Action.SHORT_DESCRIPTION, prov.getString(keyDescription));	
	}

}
