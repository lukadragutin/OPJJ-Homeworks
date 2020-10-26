package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
import java.io.OutputStream;

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

/**
 * Servlet in charge of generating a Pie Chart containing
 * information about OS usage among queried PC users
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "ChartServlet", urlPatterns = { "/reportImage" })
public class ChartServlet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("image/png");
		
		PieDataset dataset = createDataset();
		// based on the dataset we create the chart
		JFreeChart chart = createChart(dataset, "OS Usage");

		OutputStream os = resp.getOutputStream();
		
		ChartUtils.writeChartAsPNG(os, chart, 400, 400);
		
	}

	/**
	 *  Creates a chart from the given database and with the given title
	 * @param dataset Dataset to create chart from
 	 * @param title Title for the chart
	 * @return The created chart
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart(
	            title,                  // chart title
	            dataset,                // data
	            true,                   // include legend
	            true,
	            false
	        );

	        return chart;
	}

	/** Creates a database used to generate the pie chart */
	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Linux", 35);
		result.setValue("Mac", 20);
		result.setValue("Windows", 45);
		return result;
	}
}
