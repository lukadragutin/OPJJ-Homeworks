package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Tool;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Line;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.LineTool;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Triangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.TriangleTool;

/**
 * Aplikacija koja omogućava crtanje klikanjem mišem po prozoru grafičkog
 * sučelja. Moguće je crtati linije, prazne i obojane krugove. Crteže je moguće
 * uređivati mijenjajući i brišući pojedine objekte. Crteže je moguće spremati,
 * otvarati i exportati u formatu png, jpg i gif.
 * 
 * @author LukaD
 *
 */
public class JVDraw extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Trenutni objekt {@link Tool} kojim se crta po sučelju
	 */
	private static Tool state;

	/**
	 * Model koji pohranjuje objekte sa slike
	 */
	private static DrawingModel dm;

	/**
	 * Komponenta za prikaz i crtanje slike
	 */
	private static JDrawingCanvas jdc;

	/**
	 * Put otvorene slikovne datoteke
	 */
	private Path path;

	/**
	 * Komponenta za odabir boje crtanja
	 */
	private JColorArea fgColorArea;

	/**
	 * Komponenta za odabir boje ispunjenja
	 */
	private JColorArea bgColorArea;

	private Supplier<Tool> toolSupplier = new Supplier<Tool>() {
		@Override
		public Tool get() {
			return state;
		}
	};

	public JVDraw() {
		super();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("JVDraw");
		initGUI();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitOperation();
			}
		});

		setLocation(100, 50);
		setSize(800, 600);
	}

	/**
	 * Inicijalizacija grafičkog sučelja
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		fgColorArea = new JColorArea(Color.BLACK);
		bgColorArea = new JColorArea(Color.WHITE);
		JColorLabel bottomColorInfo = new JColorLabel(fgColorArea, bgColorArea);
		cp.add(bottomColorInfo, BorderLayout.PAGE_END);

		dm = new DrawingModelImpl();
		jdc = new JDrawingCanvas(toolSupplier, dm);
		cp.add(jdc, BorderLayout.CENTER);

		dm.addDrawingModelListener(new DrawingModelListener() {
			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				if (source.isModified()) {
					save.setEnabled(true);
				}
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				if (source.isModified()) {
					save.setEnabled(true);
				}
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				if (source.isModified()) {
					save.setEnabled(true);
				}
			}
		});

		cp.add(setupToolbar(), BorderLayout.PAGE_START);
		setupList(cp);
		setupMenu(cp);
	}

	/**
	 * Generira i vraća alatnu traku grafičkog sučelja
	 */
	private JToolBar setupToolbar() {
		JToolBar tbar = new JToolBar();
		tbar.setFloatable(true);
		tbar.setLayout(new FlowLayout(FlowLayout.LEFT));

		tbar.add(fgColorArea);
		tbar.add(bgColorArea);

		filledCircleAction = getfilledCircleAction(fgColorArea, bgColorArea);
		circleAction = getCircleAction(fgColorArea);
		lineAction = getLineAction(fgColorArea);

		ButtonGroup bg = new ButtonGroup();
		JToggleButton lineButton = new JToggleButton(lineAction);
		JToggleButton circleButton = new JToggleButton(circleAction);
		JToggleButton filledCircleButton = new JToggleButton(filledCircleAction);
		JToggleButton triangleButton = new JToggleButton("Triangle");
		triangleButton.addActionListener(l -> state = new TriangleTool(fgColorArea, bgColorArea, dm, jdc));
		
		bg.add(lineButton);
		bg.add(circleButton);
		bg.add(filledCircleButton);
		bg.add(triangleButton);

		tbar.addSeparator();
		tbar.add(lineButton);
		tbar.add(circleButton);
		tbar.add(filledCircleButton);
		tbar.add(triangleButton);
		return tbar;
	}

	/**
	 * Kreira i postavlja izbornik grafičkog sučelja
	 */
	private void setupMenu(Container cp) {
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		menu.add(file);

		file.add(new JMenuItem(open));
		file.add(new JMenuItem(saveAs));
		file.add(new JMenuItem(save));
		file.add(new JMenuItem(export));
		file.add(new JMenuItem(exit));

		this.setJMenuBar(menu);
	}

	/**
	 * Stvara i postavlja listu za prikaz slikovnih elemenata
	 */
	private void setupList(Container cp) {
		DrawingObjectListModel listModel = new DrawingObjectListModel(dm);
		JList<GeometricalObject> list = new JList<>(listModel);

		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!list.isFocusOwner())
					return;

				var o = list.getSelectedValue();
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					if (o == null)
						return;

					dm.remove(o);
				} else if (e.getKeyCode() == KeyEvent.VK_PLUS) {
					if (o == null)
						return;

					if (list.getSelectedIndex() == dm.getSize() - 1)
						return;
					dm.changeOrder(o, 1);
				} else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
					System.out.println(KeyEvent.getKeyText(KeyEvent.VK_MINUS));
					if (o == null)
						return;

					if (list.getSelectedIndex() == 0)
						return;
					dm.changeOrder(o, -1);
				}
			}
		});

		list.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					if (!list.isFocusOwner())
						return;

					int index = list.locationToIndex(e.getPoint());
					if (index == -1)
						return;
					var object = list.getSelectedValue();
					var editor = object.createGeometricalObjectEditor();
					editor.setPreferredSize(new Dimension(200, 250));
					if (JOptionPane.showConfirmDialog(JVDraw.this, editor) == JOptionPane.OK_OPTION) {
						try {
							editor.checkEditing();
							editor.acceptEditing();
						} catch (RuntimeException ex) {
							System.out.println(ex.getMessage());
						}
					}
				}
			}
		});
		JPanel canvas = new JPanel(new GridLayout());
		canvas.add(new JScrollPane(list));
		cp.add(canvas, BorderLayout.LINE_END);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
	}

	/**
	 * Akcija postavljanja trenutnog crtačkog alata na {@link FilledCircleTool}
	 */
	private Action filledCircleAction;

	/**
	 * Akcija postavljanja trenutnog crtačkog alata na {@link CircleTool}
	 */
	private Action circleAction;

	/**
	 * Akcija postavljanja trenutnog crtačkog alata na {@link LineTool}
	 */
	private Action lineAction;

	private Action getfilledCircleAction(IColorProvider fg, IColorProvider bg) {
		return new AbstractAction("Filled circle") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				state = new FilledCircleTool(dm, jdc, fg, bg);
			}
		};
	}

	private Action getCircleAction(IColorProvider fgProvider) {
		return new AbstractAction("Circle") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				state = new CircleTool(dm, jdc, fgProvider);
			}
		};
	}

	/**
	 * Stvara i vraca {@link #lineAction}
	 */
	private Action getLineAction(IColorProvider fgProvider) {
		return new AbstractAction("Line") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				state = new LineTool(dm, jdc, fgProvider);
			}
		};
	}

	/**
	 * Akcija za izlazak iz aplikacije
	 */
	private Action exit = new AbstractAction("Exit") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exitOperation();
		}
	};

	/**
	 * Akcija za otvaranje novog dokumenta. Dokument mora biti .jvd formata
	 */
	private Action open = new AbstractAction("Open") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			path = promptFileChooser(JFileChooser.OPEN_DIALOG, "jvd");

			if (path == null) {
				JOptionPane.showMessageDialog(JVDraw.this, "Opening canceled.");
				return;
			}

			List<String> lines;
			try {
				lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			} catch (IOException e1) {
				return;
			}

			List<GeometricalObject> obj = readFile(lines);
			
			dm.clear();
			for (var o : obj) {
				dm.add(o);
			}
			dm.clearModifiedFlag();
			save.setEnabled(false);
		}
	};

	/**
	 * Akcija za spremanje tekstovnog zapisa slike u novo odredište. Datoteka mora
	 * biti .jvd formata
	 */
	private Action saveAs = new AbstractAction("Save as..") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			path = promptFileChooser(JFileChooser.SAVE_DIALOG, "jvd");

			saveFile();
		}
	};

	/**
	 * Akcija spremanja tekstovnog zapisa slike. Datoteka mora biti .jvd formata
	 */
	private Action save = new AbstractAction("Save") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			saveOperation();
		}
	};

	/**
	 * Akcija spremanja crteža kao slikovnog dokumenta formata .png, .jpg ili .gif
	 */
	private Action export = new AbstractAction("Export") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[] formatOptions = new String[] { "PNG", "JPG", "GIF" };

		@Override
		public void actionPerformed(ActionEvent e) {
			int selection = JOptionPane.showOptionDialog(JVDraw.this, "What format do you want the picture to be?",
					"Picture export", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, formatOptions,
					formatOptions[0]);

			if (selection == JOptionPane.CLOSED_OPTION)
				return;

			String format = formatOptions[selection].toLowerCase();
			Path path = promptFileChooser(JFileChooser.SAVE_DIALOG, format);

			if (!path.toString().toLowerCase().endsWith("." + format)) {
				String pathNew = path.toString() + "." + format;
				path = Paths.get(pathNew);
			}

			Rectangle bbox = getBoundingBox();

			BufferedImage image = createImage(bbox);

			File file = path.toFile();

			if (file.exists()) {
				int select = JOptionPane.showConfirmDialog(JVDraw.this,
						"File already exists. Do you want to overwrite it?");
				if (select != JOptionPane.OK_OPTION) {
					JOptionPane.showMessageDialog(JVDraw.this, "Saving canceled.");
					return;
				}
			}
			try {
				ImageIO.write(image, format, file);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JVDraw.this, "Error exporting file.");
				return;
			}

			JOptionPane.showMessageDialog(JVDraw.this, "File exported successfully.");
		}

		private Rectangle getBoundingBox() {
			GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
			for (int i = 0; i < dm.getSize(); i++) {
				dm.getObject(i).accept(bbcalc);
			}

			return bbcalc.getBoundingBox();
		}
	};

	/**
	 * Stvara dijalog za odabir dokumenta za otvaranje ili spremanje ovisno o
	 * argumentu {@code type} Prihvaća samo datoteke sa ekstenzijom sadržanom u
	 * argumentu {@code filter}
	 * 
	 * @param type   SAVE ili OPEN dijalog
	 * @param filter Određuje tip ekstenzije koju {@link JFileChooser} prihvaća
	 * @return Put do odabrane datoteke
	 */
	private Path promptFileChooser(int type, String filter) {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Select a file.");
		jfc.setDialogType(type);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "." + filter + " files";
			}

			@Override
			public boolean accept(File f) {
				if (f.isFile()) {
					String fileName = f.toPath().getFileName().toString();
					String ext = fileName.substring(fileName.lastIndexOf('.') + 1);

					if (ext.equals(filter))
						return true;
				} else if (f.isDirectory()) {
					return true;
				}

				return false;
			}
		});
		int select;
		if (type == JFileChooser.SAVE_DIALOG) {
			select = jfc.showSaveDialog(JVDraw.this);
		} else {
			select = jfc.showOpenDialog(JVDraw.this);
		}

		if (select != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return jfc.getSelectedFile().toPath();
	}

	/**
	 * Operacija spremanja
	 */
	protected void saveOperation() {
		if (path == null) {
			path = promptFileChooser(JFileChooser.SAVE_DIALOG, "jvd");
		}

		saveFile();
	}

	/**
	 * Stvara sliku {@link DrawingModel} unutar predanog {@link Rectangle} bboxa
	 * 
	 * @param bbox Dimenzije unutar kojih se slika generira
	 * @return Sliku nastalu na osnovu elemenata {@link DrawingModel} i argumenta
	 *         {@code bbox}
	 */
	public static BufferedImage createImage(Rectangle bbox) {
		BufferedImage image = new BufferedImage(bbox.width, bbox.height, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, bbox.width, bbox.height);
		g2d.translate(-bbox.x, -bbox.y);

		GeometricalObjectPainter paint = new GeometricalObjectPainter(g2d);
		for (int i = 0; i < dm.getSize(); i++) {
			dm.getObject(i).accept(paint);
		}
		g2d.dispose();
		return image;
	}

	/**
	 * Sprema trenutno stanje slike i njezinih elemenata
	 */
	protected void saveFile() {
		if (path == null) {
			JOptionPane.showMessageDialog(this, "File not saved.");
			return;
		}

		if (!path.toString().toLowerCase().endsWith(".jvd")) {
			String pathNew = path.toString() + ".jvd";
			path = Paths.get(pathNew);
		}

		if (Files.exists(path)) {
			int select = JOptionPane.showConfirmDialog(JVDraw.this,
					"File already exists. Do you want to overwrite it?");
			if (select != JOptionPane.OK_OPTION) {
				JOptionPane.showMessageDialog(JVDraw.this, "Saving canceled.");
				return;
			}
		}

		SaveVisitor sv = new SaveVisitor();
		for (int i = 0; i < dm.getSize(); i++) {
			var o = dm.getObject(i);
			o.accept(sv);
		}

		String text = sv.toString();
		try {
			Files.writeString(path, text, StandardCharsets.UTF_8);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(JVDraw.this, "Error while saving file.");
			return;
		}

		JOptionPane.showMessageDialog(JVDraw.this, "File saved.");
		dm.clearModifiedFlag();
		save.setEnabled(false);
	}

	/**
	 * Čita tekstovnu reprezentaciju slike zadanu linijama i od nje generira objekte
	 * {@link GeometricalObject} spremljene u listu
	 * 
	 * @param lines Linije tekstovnog dokumenta koji predstavlja elemente slike
	 * @return Popis objekata isčitanih iz argumenta
	 */
	public static List<GeometricalObject> readFile(List<String> lines) {
		List<GeometricalObject> obj = new ArrayList<>();
		for (String s : lines) {
			String[] split = s.split("\\s+");
			switch (split[0]) {
			case "LINE":
				try {
					Point x = new Point(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
					Point y = new Point(Integer.parseInt(split[3]), Integer.parseInt(split[4]));
					Color color = new Color(Integer.parseInt(split[5]), Integer.parseInt(split[6]),
							Integer.parseInt(split[7]));
					obj.add(new Line(x, y, color));
				} catch (NumberFormatException e3) {
					throw new RuntimeException(e3.getMessage());
				}
				continue;
			case "CIRCLE":
				try {
					Point center = new Point(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
					int radius = Integer.parseInt(split[3]);
					Color colorC = new Color(Integer.parseInt(split[4]), Integer.parseInt(split[5]),
							Integer.parseInt(split[6]));
					obj.add(new Circle(center, radius, colorC));
				} catch (NumberFormatException e2) {
					throw new RuntimeException(e2.getMessage());

				}
				continue;
			case "FCIRCLE":
				try {
					Point centerF = new Point(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
					int radiusF = Integer.parseInt(split[3]);
					Color colorFC = new Color(Integer.parseInt(split[4]), Integer.parseInt(split[5]),
							Integer.parseInt(split[6]));
					Color colorFCbg = new Color(Integer.parseInt(split[7]), Integer.parseInt(split[8]),
							Integer.parseInt(split[9]));
					obj.add(new FilledCircle(centerF, radiusF, colorFC, colorFCbg));
				} catch (NumberFormatException e1) {
					throw new RuntimeException(e1.getMessage());
				}
				continue;
			case "FTRIANGLE":
				try {
					Point t1 = new Point(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
					Point t2 = new Point(Integer.parseInt(split[3]), Integer.parseInt(split[4]));
					Point t3 = new Point(Integer.parseInt(split[5]), Integer.parseInt(split[6]));
					Color tColor = new Color(Integer.parseInt(split[7]), Integer.parseInt(split[8]),
							Integer.parseInt(split[9]));
					Color tBgColor = new Color(Integer.parseInt(split[10]), Integer.parseInt(split[11]),
							Integer.parseInt(split[12]));
					obj.add(new Triangle(t1, t2, t3, tColor, tBgColor));
				} catch (NumberFormatException e) {
					throw new RuntimeException(e.getMessage());
				}
				continue;
			default:
				return null;
			}
		}
		return obj;
	}

	/**
	 * Izlazna operacija
	 */
	protected void exitOperation() {
		if (dm.isModified()) {
			int select = JOptionPane.showConfirmDialog(this,
					"Changes are not saved. Do you wish to save before closing?", "Unsaved changes.",
					JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (select == JOptionPane.CANCEL_OPTION || select == JOptionPane.CLOSED_OPTION) {
				return;
			} else if (select == JOptionPane.OK_OPTION)
				saveOperation();
		}
		dispose();
	}
}
