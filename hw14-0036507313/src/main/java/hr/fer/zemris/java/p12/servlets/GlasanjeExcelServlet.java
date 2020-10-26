package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import hr.fer.zemris.java.p12.PollOption;
import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOProvider;


/**
 * Servlet koji stvara XLS dokument sa resultatima glasana
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "GlasanjeExcel", urlPatterns = { "/servleti/glasanje-xls" })
public class GlasanjeExcelServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Loads band and voting info
		List<PollOption> pollOptions = (List<PollOption>) req.getSession().getAttribute("pollOptions");
		//If the attribute hasn't been set tries to load
		// options using pollID sent as parameter
		if(pollOptions == null) {
			DAO sql = DAOProvider.getDao();
			String pollID = req.getParameter("pollID");
			long id;
			try {
				id = Long.parseLong(pollID);
			} catch (NumberFormatException e) {
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}
			pollOptions = sql.getPollOptionsData(id);
		}
		
		
		var hwb = createWorkbook(pollOptions);

		//Sets content type to excel
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablicaGlasanja.xls\"");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}

	/**
	 * Stvara novi excel dokument iz predane liste podataka o opcijama anketa
	 * @param pollOptions Lista opcija anketa
	 * @return Primjerak razreda {@link HSSFWorkbook}
	 */
	private HSSFWorkbook createWorkbook(List<PollOption> pollOptions) {
		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFSheet sheet = hwb.createSheet("Voting Results");
		HSSFRow rowheadTop = sheet.createRow(0);

		var style = hwb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		
		rowheadTop.createCell(0).setCellValue("ID");
		rowheadTop.createCell(1).setCellValue("Title");
		rowheadTop.createCell(2).setCellValue("Link");
		rowheadTop.createCell(3).setCellValue("Votes");
		rowheadTop.setRowStyle(style);
		
		
		int i = 1;
		
		for (var b : pollOptions) {
			HSSFRow rowhead = sheet.createRow(i);
			
			rowhead.createCell(0).setCellValue(b.getId());						
			rowhead.createCell(1).setCellValue(b.getOptionTitle());
			rowhead.createCell(2).setCellValue(b.getOptionLink());
			rowhead.createCell(3).setCellValue(b.getVotesCount());
			rowhead.setRowStyle(style);
			
			i++;
		}
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		
		return hwb;
	}
}