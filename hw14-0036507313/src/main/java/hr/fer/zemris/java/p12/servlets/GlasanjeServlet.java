package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.Poll;
import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * Servlet koji učitava sve opcije u traženoj anketi kroz parametar (pollID)
 * i šalje ih na prikaz stranici za glasanje. <br>
 * Ako je parametar krivo zadan ili ga nema,
 * onda prikazuje korisniku da je došlo do greške
 * 
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "glasanje", urlPatterns = { "/servleti/glasanje" })
public class GlasanjeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("pollID");
		int idNum = 0;
		try {
			if (id != null) {
				idNum = Integer.parseInt(id);
			}
		} catch (NumberFormatException e) {
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		DAO sql = DAOProvider.getDao();
		
		Poll poll = sql.getPoll(idNum);
		if(poll == null) {
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}
		
		req.setAttribute("poll", poll);
		req.setAttribute("pollOptions", sql.getPollOptionsData(idNum));
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}

}
