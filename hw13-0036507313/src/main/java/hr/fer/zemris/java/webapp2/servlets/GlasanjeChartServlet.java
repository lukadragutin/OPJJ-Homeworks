package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

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

import hr.fer.zemris.java.webapp2.servlets.beans.BandInfo;

/**
 * Servlet that creates a pie chart which contains the
 * voting results about band popularity
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "GlasanjeChart", urlPatterns = { "/glasanje-grafika" })
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
	 * Creates and return a chart from the data in the given dataset
	 * and with the given title
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart(title, // chart title
				dataset, // data
				true, // include legend
				true, false);

		return chart;
	}

	/**
	 * Creates a dataset for the chart to use that contains the voting data
	 * @param req Servlet context
	 * @return Dataset containing voting data
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private PieDataset createDataset(HttpServletRequest req) throws IOException {
		DefaultPieDataset result = new DefaultPieDataset();
		Map<String, BandInfo> bands = (Map<String, BandInfo>) req.getSession().getAttribute("bands");
		Map<String, Integer> votes = (Map<String, Integer>) req.getSession().getAttribute("votes");

		for (var e : bands.entrySet()) {
			var vote = votes.get(e.getKey());
			result.setValue(e.getValue().getName(), vote == null ? 0 : vote);
		}

		return result;

	}
}
