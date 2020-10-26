package hr.fer.zemris.java.hw17.jvdraw.webapp.servlets;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw17.jvdraw.JVDraw;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.webapp.JVDFile;

@WebServlet(urlPatterns = {"/addFile"})
public class AddServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("title");
		String textString = req.getParameter("text");
		
		
		List<GeometricalObject> x;
//		try {
			x = JVDraw.readFile(Arrays.asList(textString.split("\n")));
//		} catch (Exception e) {
//			req.setAttribute("error", "Invalid syntax.");
//			req.getRequestDispatcher("main").forward(req, resp);
//			return;
//		}
		JVDFile file = new JVDFile(name, x);
		
		String pathString = req.getServletContext().getRealPath("/WEB-INF/images");
		JVDFile.saveJvdFile(Paths.get(pathString), file);
		resp.sendRedirect("main");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
