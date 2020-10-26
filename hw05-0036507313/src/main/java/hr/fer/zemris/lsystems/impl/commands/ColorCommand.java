package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Naredba mijenjanja boje L-sustava
 * @author Luka
 *
 */
public class ColorCommand implements Command {

	/**
	 * Boja u koju mijenjamo
	 */
	private Color color;

	/**
	 * Nova naredba koja mijenja boju L-sustava u zadanu {@code color}.
	 * @param color Nova boja L-sustava
	 * @throws NullPointerException ako je boja <code>null</code>
	 */
	public ColorCommand(Color color) {
		super();
		this.color = Objects.requireNonNull(color);
	}
	
	/**
	 * Dohvaća trenutno stanje L-sustava i mijenja boju
	 * @param ctx kontekst L-sustava
	 * @param painter GUI operator L-sustava
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color);
	}
}
