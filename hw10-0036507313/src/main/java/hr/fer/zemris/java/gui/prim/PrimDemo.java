package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
/**
 * Graficko sucelje koje generira proste brojeve po redu
 * uzlazno od broja 1 i prikazuje ih u dvije tablične liste.
 * Korisnik pokrece generiranje sljedeceg prostog broja
 * klikom na gumb na dnu prozora.
 * @author Luka Dragutin
 *
 */
public class PrimDemo extends JFrame {
	
	public PrimDemo() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI();
		setSize(500, 500);
	}

	//Inicijalizacija grafickih komponenata programa
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);

		
		JButton next = new JButton("sljedeci");
		next.addActionListener((l) ->  model.next());
		cp.add(next, BorderLayout.PAGE_END);
		
		JPanel canvas = new JPanel();
		canvas.setLayout(new GridLayout(1, 2));
		
		canvas.add(new JScrollPane(list1));
		canvas.add(new JScrollPane(list2));
		cp.add(canvas, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));

	}

}
