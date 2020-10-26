package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that lists all the sinus and cosinus values of angles
 * between parameters a and b (a has to be lower than b)
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "trigonometric", urlPatterns = { "/trigonometric" })
public class TrigonometryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String a = req.getParameter("a");
		String b = req.getParameter("b");

		int varA = 0;
		int varB = 360;
		try {
			if (a != null) {
				varA = Integer.parseInt(a);
			}
		} catch (Exception e) {
		}

		try {
			if (b != null) {
				varB = Integer.parseInt(b);
			}
		} catch (Exception e) {
		}

		if (varA > varB) {
			int temp = varA;
			varA = varB;
			varB = temp;
		}

		if (varB > varA + 720) {
			varB = varA + 720;
		}

		TreeMap<Integer, Values> trg = new TreeMap<>();
		for (int i = varA; i < varB; i++) {
			double rad = Math.toRadians(i);
			trg.put(i, new Values(String.format("%.4f", Math.cos(rad)), String.format("%.4f", Math.sin(rad))));
		}

		req.setAttribute("trigonometry", trg);

		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
}
