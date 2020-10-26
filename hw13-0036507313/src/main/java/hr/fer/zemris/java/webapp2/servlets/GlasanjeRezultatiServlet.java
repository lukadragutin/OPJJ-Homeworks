package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet used to load the voting and band information and than dispatches
 * the loading od the voting results page
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "GlasanjeRezultati", urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("bands", LoadUtil.loadBands(req));
		req.setAttribute("votes", LoadUtil.loadVotes(req));

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
	
	
}
