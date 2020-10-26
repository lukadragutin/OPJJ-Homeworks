package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;


/**
 * Localized variation of the {@link JMenu} component that uses the 
 * {@link ILocalizationProvider} to get the current localized string value of its key 
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class LJMenu extends JMenu {

	/**
	 * Provider for localization services
	 */
	private ILocalizationProvider prov;
	
	/**
	 * Key for getting localized text value
	 */
	private String key;
	
	/**
	 * Listener for the {@link ILocalizationProvider} that makes sure the {@link LJMenu} 
	 * localized text i current with the current language settings
	 */
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			setText(prov.getString(key));
		}
	};
	
	public LJMenu(String key, ILocalizationProvider lp) {
		super();
		this.key = key;
		prov = lp;
		setText(prov.getString(key));
		prov.addLocalizationListener(listener);
	}
	
}
