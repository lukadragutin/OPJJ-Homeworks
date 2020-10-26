package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Line;


/**
 * Implementacija sučelja {@link GeometricalObjectVisitor} koja objekte
 * {@link GeometricalObject} pretvara u tekstovni zapis njihovih karakteristika
 * @author LukaD
 *
 */
public class SaveVisitor implements GeometricalObjectVisitor {

	private StringBuilder sb = new StringBuilder();
	
	/**
	 * Zapisuje karakteristike objekta {@link Line} u sljedećem formatu <br>
	 * LINE prvaTocka.x prvaTocka.y drugaTocka.x drugaTocka.y R G B
	 */
	@Override
	public void visit(Line line) {
		Color color = line.getColor();
		sb.append(String.format("LINE %s %s %s %s %s %s %s\n", line.getX().x, line.getX().y, line.getY().x, line.getY().y, color.getRed(), color.getGreen(), color.getBlue()));
	}

	
	/**
	 * Zapisuje karakteristike objekta {@link Circle} u sljedećem formatu <br>
	 * CIRCLE središte.x središte.y radijus R G B
	 */
	@Override
	public void visit(Circle circle) {
		Color color = circle.getColor();
		sb.append(String.format("CIRCLE %s %s %s %s %s %s\n", circle.getCenter().x, circle.getCenter().y, circle.getRadius(), color.getRed(), color.getGreen(), color.getBlue()));
	}

	
	/**
	 * Zapisuje karakteristike objekta {@link FilledCircle} u sljedećem formatu <br>
	 * FCIRCLE središte.x središte.y radijus R G B Rbg Gbg Bbg (bg označava boju ispunjenja)
	 */
	@Override
	public void visit(FilledCircle filledCircle) {
		Color color = filledCircle.getColor();
		Color bgColor = filledCircle.getBgColor();
		sb.append(String.format("FCIRCLE %s %s %s %s %s %s %s %s %s\n", filledCircle.getCenter().x, filledCircle.getCenter().y, filledCircle.getRadius(), color.getRed(), color.getGreen(), color.getBlue(), bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));
	}
	
	@Override
	public String toString() {
		return sb.toString().trim();
	}

}
