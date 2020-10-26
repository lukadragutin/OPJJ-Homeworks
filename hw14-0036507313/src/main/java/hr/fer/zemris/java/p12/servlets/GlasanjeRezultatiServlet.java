package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.PollOption;
import hr.fer.zemris.java.p12.dao.sql.SQLDAO;

/**
 * Servlet korišten za učitavanje podataka o traženoj anketi te 
 * preusmjeravanja tih podataka na stranicu sa rezultatima
 * 
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "GlasanjeRezultati", urlPatterns = { "/servleti/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SQLDAO sql = new SQLDAO();
		String obj = req.getParameter("pollID");

		long id;
		try {
			id = Long.parseLong(obj);
		} catch (Exception e) {
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		var poll = sql.getPoll(id);
		if (poll == null) {
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp");
		}

		req.setAttribute("poll", poll);

		var pollOptions = sql.getPollOptionsData(id);
		List<PollOption> best = new ArrayList<>();
		
		if (pollOptions != null && !pollOptions.isEmpty()) {
			pollOptions.sort((e1, e2) -> Long.valueOf(e2.getVotesCount()).compareTo(e1.getVotesCount()));

			long max = pollOptions.get(0).getVotesCount();
			
			for (var opt : pollOptions) {
				if(opt.getVotesCount() == max) {
					best.add(opt);
				}
				else {
					break;
				}
			}
		}
		req.setAttribute("pollOptions", pollOptions);
		req.setAttribute("best", best);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

}
