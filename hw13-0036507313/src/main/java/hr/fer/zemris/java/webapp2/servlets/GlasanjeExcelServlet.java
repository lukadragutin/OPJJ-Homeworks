package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.webapp2.servlets.beans.BandInfo;

/**
 * Servlet used to create an XLS file with voting results
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "GlasanjeExcel", urlPatterns = { "/glasanje-xls" })
public class GlasanjeExcelServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Loads band and voting info
		Map<String, Integer> votes =(Map<String, Integer>) req.getSession().getAttribute("votes");
		Map<String, BandInfo> bands = (Map<String, BandInfo>) req.getSession().getAttribute("bands");

		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFSheet sheet = hwb.createSheet("Voting Results");
		HSSFRow rowheadTop = sheet.createRow(0);

		rowheadTop.createCell(0).setCellValue("Bands");
		rowheadTop.createCell(1).setCellValue("Votes");
		int i = 0;
		for (var b : bands.entrySet()) {
			HSSFRow rowhead = sheet.createRow(i);
			rowhead.createCell(0).setCellValue(b.getValue().getName());
			var vote = votes.get(b.getKey());
			rowhead.createCell(1).setCellValue(vote == null ? "0" : vote.toString());
			i++;
		}

		//Sets content type to excel
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablicaGlasanja.xls\"");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}
}