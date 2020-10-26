package hr.fer.zemris.java.hw17.jvdraw.webapp.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.impl.Line;
import hr.fer.zemris.java.hw17.jvdraw.webapp.JVDFile;

@WebServlet(urlPatterns = {"/open"})
public class OpenFileServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String name = req.getParameter("name");
		if (name == null) {
			error(req, resp);
			return;
		}
		
		String pathString = req.getServletContext().getRealPath("/WEB-INF/images");
		Path home = Paths.get(pathString);
		
		var file = home.resolve(name);
		
		if(!Files.exists(file)) {
			error(req, resp);
			return;
		}
		
		JVDFile jvdFile = JVDFile.loadJvdFile(file);

		var objects = jvdFile.getLines();
		
		int line = 0;
		int circles = 0;
		int fCircles = 0;
		int triangles = 0;
		
		for(var o : objects) {
			if(o instanceof Line) {
				line++;
			}
			else if(o instanceof FilledCircle){
				fCircles++;
			} else if(o instanceof Circle) {
				circles++;
			} else {
				triangles++;
			}
		}
		
		
		req.setAttribute("lines", line);
		req.setAttribute("circles", circles);
		req.setAttribute("fCircles", fCircles);
		req.setAttribute("triangles", triangles);
		req.setAttribute("file", jvdFile);
		
		req.getRequestDispatcher("WEB-INF/pages/prikaz.jsp").forward(req, resp);
		
	}

	private void error(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/pages/error.jsp").forward(req, resp);
	}

}
