package hr.fer.zemris.java.gui.charts;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * Graficko sucelje koje cita datoteku sa podacima o 
 * grafu sa adrese predane preko naredbenog retka i 
 * crta taj graf na novootvorenom prozoru.
 * @author Luka Dragutin
 */
@SuppressWarnings("serial")
public class BarChartDemo extends JFrame {

	/** Primjerak razreda {@link BarChart} po kojem se radi slika grafa */
	private BarChart chart;

	public BarChartDemo(BarChart chart) {
		super();
		this.chart = chart;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI();
		setSize(500, 500);
	}

	/**
	 * Inicijaliziranje slike
	 */
	private void initGUI() {
		var cp = getContentPane();
		cp.add(new BarChartComponent(chart));
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Wrong number of arguments.");
			return;
		}
		List<String> s;
		try {
			s = Files.lines(Paths.get(args[0]), Charset.forName("UTF-8")).collect(Collectors.toList());
		} catch (IOException e) {
			System.out.println("Error reading file.");
			return;
		}

		String[] data = s.get(2).split("\\s+");
		ArrayList<XYValue> xy = new ArrayList<>();
		for (String entry : data) {
			try {
				int x = Integer.parseInt(entry.substring(0, entry.indexOf(',')));
				int y = Integer.parseInt(entry.substring(entry.indexOf(',') + 1));
				xy.add(new XYValue(x, y));
			} catch (NumberFormatException e) {
				System.out.println("Invalid chart data format.");
				return;
			}
		}

		BarChart chart;
		try {
			int yMin = Integer.parseInt(s.get(3));
			int yMax = Integer.parseInt(s.get(4));
			int yStep = Integer.parseInt(s.get(5));
			chart = new BarChart(xy, s.get(0), s.get(1), yMin, yMax, yStep);
		} catch (NumberFormatException e) {
			System.out.println("Invalid chart data format.");
			return;
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return;
		}

		SwingUtilities.invokeLater(() -> new BarChartDemo(chart).setVisible(true));

	}

}
