package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet in charge of generating an XLS document that contains powers
 * of the numbers between a and b given as parameters (limited to interval [-100, 100])
 * from 1 to the number n given as parameter (limited to interval [1,5])
 * @author Luka Dragutin
 *
 */
@WebServlet(name="excel", urlPatterns={"/powers"})
public class ExcelServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int a, b, n;
	private boolean error;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		error = false;
		
		String varA = req.getParameter("a");
		String varB = req.getParameter("b");
		String varN = req.getParameter("n");

		testParameters(varA, varB, varN, req, resp);
		if(error) return;
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		
		for(int i = 1; i <= n; i++) {
			HSSFSheet sheet = hwb.createSheet("Power to the " + i + " potention.");
			HSSFRow rowheadTop = sheet.createRow(0);
			rowheadTop.createCell(0).setCellValue("	X");
			rowheadTop.createCell(1).setCellValue("	X^"+i);
			for(int j = a; j <= b; j++) {
				HSSFRow rowhead = sheet.createRow(j - a + 1);
				rowhead.createCell(0).setCellValue(j);
				rowhead.createCell(1).setCellValue(Math.pow(j, i));
			}
		}
		
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}

	/**
	 * 
	 * @param varA parameter a
	 * @param varB parameter b
	 * @param varN max power
	 * @param req Request of the current servlet
	 * @param resp Response of the current servlet
	 * @throws ServletException
	 * @throws IOException 
	 */
	private void testParameters(String varA, String varB, String varN, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(varA == null || varB == null || varN == null) {
			error(req, resp);
			return;
		}
		
		try {
			a = Integer.parseInt(varA);
			b = Integer.parseInt(varB);
			n = Integer.parseInt(varN);
		} catch(Exception e) {
			error(req, resp);
			return;
		}
		
		if(a > b) {
			int temp = a;
			a = b;
			b = temp;
		}
		
		if(Math.abs(a) > 100 || Math.abs(b) > 100 || n > 5 || n < 1) {
			error(req, resp);
			return;
		}
		
		
	}

	private void error(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/pages/error.jsp").forward(req, resp);
		error = true;
	}
}