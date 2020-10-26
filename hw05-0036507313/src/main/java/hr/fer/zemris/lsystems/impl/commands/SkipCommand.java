package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Naredba pomicanja kornjače L-sustava
 * @author Luka Dragutin
 */
public class SkipCommand implements Command{

	/**Kvantifikator pomaka*/
	private double step;
	
	/**
	 * Nova naredba za pomakom za {@code step} puta jedinicnim pomakom kornjače
	 * @param step Koeficijent jediničnog pomaka
	 */
	public SkipCommand(double step) {
		super();
		this.step = step;
	}

	/**
	 * Pomiče kornjaču za {@code step} puta jedinični pomak
	 * @param ctx kontekst L-sustava
	 * @param painter GUI operator L-sustava
	 */
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();
		Vector2D position = state.getPosition().copy();
		
		double scaler = step*state.getUnitLength();
		position.translate(state.getOrientation().scaled(scaler));
		
		state.setPosition(position);
	}
}