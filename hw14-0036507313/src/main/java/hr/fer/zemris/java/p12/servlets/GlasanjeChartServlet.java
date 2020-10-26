package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import hr.fer.zemris.java.p12.PollOption;

/**
 * Servlet koji stvara 'pita-graf' sa rezultatima
 * glasanja u anketi
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "GlasanjeChart", urlPatterns = { "/servleti/glasanje-grafika" })
public class GlasanjeChartServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");

		PieDataset dataset = createDataset(req);
		// based on the dataset we create the chart
		JFreeChart chart = createChart(dataset, "Pie-chart");

		OutputStream os = resp.getOutputStream();

		ChartUtils.writeChartAsPNG(os, chart, 400, 400);
	}

	/**
	 * Stvara i vraća graf od predanih podataka {@code dataset} i postavlja
	 * naslov na {@code title}
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart(title, // chart title
				dataset, // data
				true, // include legend
				true, false);

		return chart;
	}

	/**
	 * Generira podatke za 'pie-chart' iz podataka o glasanju
	 * @param req Servlet contekst 
	 * @return podatke o glasanju kao primjerak razreda {@link PieDataset}
	 */
	@SuppressWarnings("unchecked")
	private PieDataset createDataset(HttpServletRequest req) {
		DefaultPieDataset result = new DefaultPieDataset();
		List<PollOption> pollOptions = (List<PollOption>) req.getSession().getAttribute("pollOptions");

		for (var e : pollOptions) {
			result.setValue(e.getOptionTitle(), e.getVotesCount());
		}

		return result;

	}
}
