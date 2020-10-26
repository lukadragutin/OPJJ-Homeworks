package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that loads the bands information and dispatches the loading
 * of the voting page
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "glasanje", urlPatterns = { "/glasanje" })
public class GlasanjeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("bands", LoadUtil.loadBands(req));
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
}
