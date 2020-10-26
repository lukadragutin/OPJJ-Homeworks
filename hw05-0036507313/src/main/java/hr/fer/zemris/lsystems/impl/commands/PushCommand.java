package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Naredba koja stvara kopiju trenutnog stanja i stavlja ju na
 * vrh stoga konteksta L-sustava
 * @author Luka Dragutin
 */
public class PushCommand implements Command{

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.pushState(ctx.getCurrentState().copy());
	}
	
}
