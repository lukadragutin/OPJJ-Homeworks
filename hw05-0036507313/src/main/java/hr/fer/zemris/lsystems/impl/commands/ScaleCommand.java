package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Naredba skaliranja veličine jediničnog pomaka L-sustava
 * @author Luka Dragutin
 *
 */
public class ScaleCommand implements Command{

	/**Vrijednost za skaliranje jediničnog pomaka */
	private double scale;

	public ScaleCommand(double scale) {
		super();
		this.scale = scale;
	}

	/**
	 * Mijenja vrijednost jedničnog pomaka tako da trenutnu
	 * vrijednost množi sa vrijednošću {@code scale}
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		double scaled = ctx.getCurrentState().getUnitLength() * scale;
		ctx.getCurrentState().setUnitLength(scaled);
	}
}
