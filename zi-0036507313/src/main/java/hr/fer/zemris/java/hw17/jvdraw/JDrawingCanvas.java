package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.function.Supplier;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Tool;


/**
 * Swing komponenta koja omogučava crtanje i prikaz objekata tipa {@link GeometricalObject} pomoću
 * objekata koji implementiraju sučelje {@link Tool}. Objekte koje prikazuje preuzima iz objekta
 * {@link DrawingModel} koji mu javlja sve promjene nad svojim elementima koje se evidentiraju
 * pozivom metode {@link #repaint()} <br>
 * Crtanje po površini komponente implementira razred {@link Tool}
 * @author LukaD
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Trenutni {@link Tool} za crtanje
	 */
	private Supplier<Tool> state;
	
	/**
	 * Model koji pohranjuje sve objekte koje treba prikazati
	 */
	private DrawingModel dm;

	/**
	 * Primjerak razreda {@link MouseListener} koji 
	 * bilježi pokrete i akcije miša na komponenti pa ih proslijeđuje
	 * trenutnom {@link Tool} objektu na crtanje
	 */
	private MouseListener ml = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (state.get() == null)
				return;
			state.get().mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (state.get() == null)
				return;
			state.get().mousePressed(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			if (state.get() == null)
				return;
			state.get().mouseReleased(e);
		};
	};

	/**
	 * Primjerak razreda {@link MouseListener} koji 
	 * bilježi pokrete i akcije miša na komponenti pa ih proslijeđuje
	 * trenutnom {@link Tool} objektu na crtanje
	 */
	private MouseMotionListener mml = new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (state.get() == null)
				return;
			state.get().mouseMoved(e);
		};

		public void mouseDragged(MouseEvent e) {
			if (state.get() == null)
				return;
			state.get().mouseDragged(e);
		};
	};

	public JDrawingCanvas(Supplier<Tool> state, DrawingModel dm) {
		this.state = state;
		this.dm = dm;
		dm.addDrawingModelListener(this);
		addMouseListener(ml);
		addMouseMotionListener(mml);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		var painter = new GeometricalObjectPainter(g2d);
		for (int i = 0; i < dm.getSize(); i++) {
			GeometricalObject object = dm.getObject(i);
			object.accept(painter);
		}

		Tool currentState = state.get();
		if (currentState != null) {
			currentState.paint(g2d);
		}
	}

}
