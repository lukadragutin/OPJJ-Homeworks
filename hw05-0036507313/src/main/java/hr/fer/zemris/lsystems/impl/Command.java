package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.Painter;

/**
 * Sucelje koje definira metode naredbi u L-Sustavima
 * razreda {@link LSystem}
 * @author Luka Dragutin
 *
 */
public interface Command {

	/**
	 * Izvrsava naredbu
	 * @param ctx Kontekst L-sustava (stog)
	 * @param painter Primjerak objekta koji operira nad
	 * GUI dijelom L-sustava
	 */
	public void execute(Context ctx, Painter painter);
}
