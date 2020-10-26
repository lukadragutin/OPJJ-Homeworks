package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * A {@link LocalizationProviderBridge} to communicate between
 * graphical components and the {@link ILocalizationProvider}
 * @author Luka Dragutin
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	public FormLocalizationProvider(ILocalizationProvider lp) {
		super(lp);
	}
	
	/**
	 * Adds a window listener to the given subject frame that
	 * disconnects the bridge from the {@link ILocalizationProvider} upon window closing
	 * @param lp {@link ILocalizationProvider}
	 * @param subject Frame that uses the {@link LocalizationProvider}
	 */
	public FormLocalizationProvider(ILocalizationProvider lp, JFrame subject) {
		super(lp);
		subject.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				connect();
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				disconnect();
			}
		
		});
	}

}
