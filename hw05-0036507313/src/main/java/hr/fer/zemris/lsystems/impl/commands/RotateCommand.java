package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Naredba rotiranja trenutne orijentacije
 * kornjače u matematički pozitivnom smjeru
 * @author Luka Dragutin
 */
public class RotateCommand implements Command {

	/**Kut za koji se rotira kornjača*/
	private double angle;
	
	public RotateCommand(double angle) {
		this.angle = Math.toRadians(angle);
	}
	
	/**
	 *Mijenja orijentaciju kornjače za vrijednost kuta {@code angle} 
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().getOrientation().rotate(angle);
	}
}
