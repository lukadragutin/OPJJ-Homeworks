package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet in charge of changing the current background color used
 * across the site to the on given as parameter. If no parameter is
 * given sets the color to white
 * @author Luka Dragutin
 *
 */
//@WebServlet(name="colorServlet", urlPatterns= {"/setcolor"})
public class ColorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		var session = req.getSession();
		String color =  req.getParameter("pickedBgCol");
		if (color == null) {
			color = "FFFFFF";
		}
		session.setAttribute("bgColor", color);
		
		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
}
