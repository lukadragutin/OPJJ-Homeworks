package coloring.algorithms;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import marcupic.opjj.statespace.coloring.Picture;


/**
 * Implementira sve operacije potrebne za algoritam crtanja
 * @author Luka Dragutin
 *
 */
public class Coloring implements Consumer<Pixel>, Function<Pixel, List<Pixel>>, Predicate<Pixel>, Supplier<Pixel> {

	/**
	 * Pocetni pixel
	 */
	private Pixel reference;
	
	/**
	 * Slika/okvir
	 */
	private Picture picture;
	
	/**
	 * Boja kojom bojamo
	 */
	private int fillColor;
	
	/**
	 * Boja povrsine koju bojamo
	 */
	private int refColor;
	
	public Coloring(Pixel reference, Picture picture, int fillColor) {
		this.reference = Objects.requireNonNull(reference);
		this.picture = Objects.requireNonNull(picture);
		this.fillColor = fillColor;
		refColor = picture.getPixelColor(reference.getX(), reference.getY());
	}

	@Override
	public Pixel get() {
		return reference;
	}

	@Override
	public boolean test(Pixel t) {
		return picture.getPixelColor(t.getX(), t.getY()) == refColor;
	}

	@Override
	public List<Pixel> apply(Pixel t) {
		LinkedList<Pixel> list = new LinkedList<>();
		list.add(new Pixel((t.getX() + 1) >= picture.getWidth() ? t.getX() : t.getX() + 1, t.getY()));
		list.add(new Pixel(t.getX(), (t.getY() + 1) >= picture.getHeight() ? t.getY() : t.getY() + 1));
		list.add(new Pixel((t.getX() - 1) < 0 ? t.getX() : t.getX() - 1, t.getY()));
		list.add(new Pixel(t.getX(), (t.getY() - 1) < 0 ? t.getY() : t.getY() - 1));
		return list;
	}

	@Override
	public void accept(Pixel t) {
		picture.setPixelColor(t.getX(), t.getY(), fillColor);
	}

}
