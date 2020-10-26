package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Objects;

import javax.swing.JLabel;

/**
 * Localization adjusted {@link JLabel}
 * @author Luka Dragutin
 *
 */
public class LJLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Localization key */
	private String key;
	
	/**
	 * Localization provider
	 */
	private ILocalizationProvider prov;
	
	/**Listener that monitors localization changes  */
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			updateLabel();
		}
	};
	
	public LJLabel(String key, ILocalizationProvider lp) {
		super();
		prov = Objects.requireNonNull(lp);
		this.key = Objects.requireNonNull(key);
		prov.addLocalizationListener(listener);
	}

	/**Updated the label text using
	 * its localization key 
	 */
	private void updateLabel() {
		setText(prov.getString(key));
	}
}
