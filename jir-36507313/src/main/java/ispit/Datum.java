package ispit;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Datum extends JFrame {

	DefaultListModel<LocalDate> list = new DefaultListModel<>();
	
	public Datum() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI();
		pack();
	}

	private void initGUI() {
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		menu.add(file);
		
		file.add(new JMenuItem(open));
		
		this.setJMenuBar(menu);
		
		var cp = this.getContentPane();
		//cp.setLayout();
		
		JPanel centerJPanel = new JPanel();
		centerJPanel.setLayout(new GridLayout(0, 2));
		
		JList<LocalDate> jlist = new JList<>(list);
		//JPanel canvas = new JPanel();
		JScrollPane scrollPane = new JScrollPane(jlist);
		//canvas.add(scrollPane);
		centerJPanel.add(scrollPane);
		
		JPanel rightJPanel = new JPanel();
		DateLabel label = new DateLabel();

		rightJPanel.add(label);
		jlist.addListSelectionListener(label);
		centerJPanel.add(rightJPanel);
		
		cp.add(centerJPanel);
	}

	private Action open = new AbstractAction("Open") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Path path = promptFileChooser(JFileChooser.OPEN_DIALOG, "jvd");

			if (path == null) {
				JOptionPane.showMessageDialog(Datum.this, "Opening canceled.");
				return;
			}

			List<String> lines;
			try {
				lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			} catch (IOException e1) {
				return;
			}
			
			lines.removeIf(l -> l.isBlank());
			lines.removeIf(l -> l.startsWith("#"));
			
			List<LocalDate> dates = new ArrayList<LocalDate>();
			list.clear();
			lines.forEach(l -> list.addElement(LocalDate.parse(l)));
			
			//list.updateDates(dates);
			//list.print();
		}
	};

	private Path promptFileChooser(int type, String filter) {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Select a file.");
		jfc.setDialogType(type);

		int select = jfc.showOpenDialog(Datum.this);

		if (select != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return jfc.getSelectedFile().toPath();
	}
	
	public static void main (String[] args) {
		SwingUtilities.invokeLater(() -> new Datum().setVisible(true));
	}
}
