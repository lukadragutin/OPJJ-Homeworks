package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that register a vote for a band whose id is given as parameter, and
 * then forwards the request to the site for displaying voting results
 * @author Luka Dragutin
 *
 */
@WebServlet(name = "GlasanjeGlasaj", urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path filePath = Paths.get(fileName);
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);	
		}
		

		String file = Files.readString(filePath);
		String index = req.getParameter("id");
		StringBuilder fileSb = new StringBuilder(file);
		if (file.contains(index + "\t")) {
			int i = file.indexOf(index);
			int votesIndex = i + index.length() + 1;
			int end = file.indexOf('\n', i);

			if(votesIndex >= end) votesIndex = end - 1;
			fileSb.delete(votesIndex, end);
			String votes = file.substring(votesIndex, end);
			int voteNum = Integer.parseInt(votes) + 1;
			fileSb.insert(votesIndex, String.valueOf(voteNum));
		} else {
			fileSb.append(index + "\t1\n");
		}

		Files.writeString(filePath, fileSb.toString());

		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
}
