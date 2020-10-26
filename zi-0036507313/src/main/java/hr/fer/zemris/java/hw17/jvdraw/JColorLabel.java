package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * Swing komponenta koja vrši prikaz trenutno odabrane pozadinske i glavne boje u
 * {@link JVDraw} aplikaciji za crtanje. <br>
 * Ovaj razred je promatrač nad razredom {@link IColorProvider} koji ga obavještava o promjeni boje.
 * @author LukaD
 *
 */
public class JColorLabel extends JLabel implements ColorChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * trenutna glavna boja
	 */
	private Color foregroundColor;
	
	/**
	 * Trenutna pozadinska boja
	 */
	private Color backgroundColor;
	
	/**
	 * Subjekt koji određuje trenutnu pozadinsku boju
	 */
	private IColorProvider bgColorProvider;

	public JColorLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		super();

		this.bgColorProvider = bgColorProvider;

		fgColorProvider.addColorChangeListener(this);
		bgColorProvider.addColorChangeListener(this);

		foregroundColor = fgColorProvider.getCurrentColor();
		backgroundColor = bgColorProvider.getCurrentColor();

		updateInfo();
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		if (source.equals(bgColorProvider)) {
			backgroundColor = newColor;
		} else {
			foregroundColor = newColor;
		}
		
		updateInfo();
	}

	/**
	 * Osvježi prikaz boja na grafičkom sučelju (postavi tekst komponente)
	 */
	private void updateInfo() {
		setText(String.format("Foreground color: (%s, %s, %s), background color: (%s, %s, %s).", foregroundColor.getRed(),
				foregroundColor.getGreen(), foregroundColor.getBlue(), backgroundColor.getRed(),
				backgroundColor.getGreen(), backgroundColor.getBlue()));
	}
}
