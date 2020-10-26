package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;


/**
 * Sučelje koje modelira postupke crtanja koristeći
 * registrirane pokrete i akcije miša nad komponentom koja joj
 * dojavljuje promjene. 
 * @author LukaD
 *
 */
public interface Tool {

	/**
	 * Postupak tijekom držanja pritiska miša
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Postupak pri puštanju pritiska miša
	 */
	public void mouseReleased(MouseEvent e);

	/**
	 * Postupak nakon klika mišom
	 */
	public void mouseClicked(MouseEvent e);

	/**
	 * Postupak nakon pomicanja miša
	 */
	public void mouseMoved(MouseEvent e);

	/**
	 * Postupak nakon držanja klika i pomicanja miša
	 */
	public void mouseDragged(MouseEvent e);
	
	/**
	 * Crtanje trenutnih vrijednosti
	 */
	public void paint(Graphics2D g2d);
	
}
