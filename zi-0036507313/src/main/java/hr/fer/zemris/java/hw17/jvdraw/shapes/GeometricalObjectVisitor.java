package hr.fer.zemris.java.hw17.jvdraw.shapes;

import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Line;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Triangle;


/**
 * Sučelje koje modelira objekt za iteriranje po primjercima {@link GeometricalObject}
 * i obavljanja radnji specifičnih za pojedini objekt
 * @author LukaD
 *
 */
public interface GeometricalObjectVisitor {

	/**
	 * Izvršava radnju na objektom {@link Line}
	 */
	void visit(Line line);

	/**
	 * Izvršava radnju nad objektom {@link Circle}
	 * @param circle
	 */
	void visit(Circle circle);
	
	/**
	 * Izvršava radnju nad objektom {@link FilledCircle}
	 */
	void visit(FilledCircle filledCircle);
	
	
	void visit(Triangle triangle);
}
