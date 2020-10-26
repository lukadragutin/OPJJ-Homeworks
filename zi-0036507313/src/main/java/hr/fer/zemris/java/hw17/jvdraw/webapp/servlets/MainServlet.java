package hr.fer.zemris.java.hw17.jvdraw.webapp.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw17.jvdraw.webapp.JVDFile;

@WebServlet(urlPatterns = { "/main" })
public class MainServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String pathString = req.getServletContext().getRealPath("/WEB-INF/images");
		List<JVDFile> files = new ArrayList<>();

		JVDFile.loadFiles(files, pathString);
		req.setAttribute("files", files);

		req.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
