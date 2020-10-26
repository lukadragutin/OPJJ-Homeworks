package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.sql.SQLDAO;

/**
 * Servlet koji opciji za glasanje čiji je identifikator predan
 * kao parametar bilježi glas pomoću metode {@link SQLDAO#voteFor(long)}
 * te potom učitava stranicu sa rezultatima ankete
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "GlasanjeGlasaj", urlPatterns = { "/servleti/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String id = req.getParameter("id");
		String pollID = req.getParameter("pollID");

		int idNum;
		try {
			idNum = Integer.parseInt(id);
		} catch (Exception e) {
			req.getRequestDispatcher("/WEB-INT/pages/error.jsp").forward(req, resp);
			return;
		}
		
		SQLDAO sql = new SQLDAO();
		sql.voteFor(idNum);
		
		resp.sendRedirect("glasanje-rezultati?pollID=" + pollID);
	}
}
