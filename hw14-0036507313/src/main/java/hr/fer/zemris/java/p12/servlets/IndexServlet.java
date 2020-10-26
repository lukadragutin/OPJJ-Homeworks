package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.Poll;
import hr.fer.zemris.java.p12.dao.sql.SQLDAO;


/**
 * Servlet koji učitava podatke o anketama i šalje
 * ih kao atribute za prikaz na naslovnici
 * @author Luka Dragutin
 *
 */
@WebServlet(name="IndexServlet", urlPatterns = {"/servleti/index.html"})
public class IndexServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SQLDAO sql = new SQLDAO();
		List<Poll> polls =  sql.getPollData();
		req.setAttribute("polls", polls);
		
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
