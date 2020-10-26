package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Naredba koja briše trenutno stanje sa stoga
 * @author Luka Dragutin
 */
public class PopCommand implements Command {

	public void execute(Context ctx, Painter painter) {
		ctx.popState();
	}
}
