package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Naredba crtanja u L-sustavu gdje se kornjača iz trenutnog
 * stanja pomiče prema naprijed za definiranu vrijednost
 * @author Luka Dragutin
 */
public class DrawCommand implements Command {

	/**Kvantifikator pomaka*/
	private double step;

	/**
	 * Nova naredba za pomakom za {@code step} puta jedinicnim pomakom kornjače
	 * i crtanjem puta na GUI L-sustava
	 * @param step Koeficijent jediničnog pomaka
	 */
	public DrawCommand(double step) {
		super();
		this.step = step;
	}
	
	/**
	 * Pomiče kornjaču za {@code step} puta jedinični pomak i crta prijeđeni put
	 * @param ctx kontekst L-sustava
	 * @param painter GUI operator L-sustava
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Vector2D position = ctx.getCurrentState().getPosition().copy();
		
		double x = position.getX();
		double y = position.getY();
		
		new SkipCommand(step).execute(ctx, painter);
		
		TurtleState newState = ctx.getCurrentState();
		
		painter.drawLine(newState.getPosition().getX(), newState.getPosition().getY(),
						 x, y, ctx.getCurrentState().getColor(), 1f);
	}
}
