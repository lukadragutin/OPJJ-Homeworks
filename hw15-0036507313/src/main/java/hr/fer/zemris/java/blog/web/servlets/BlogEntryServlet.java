package hr.fer.zemris.java.blog.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.blog.model.util.Util;

@WebServlet(urlPatterns = { "/servleti/author/*/*" })
public class BlogEntryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String info = req.getPathInfo().substring(1);
		String[] split = info.split("/");
		
		Long eID;
		try {
			eID = Long.parseLong(split[1]);
		} catch (NumberFormatException e) {
			Util.sendError(req, resp, "Invalid entry request.");
			return;
		}

		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(eID);
		BlogUser user = DAOProvider.getDAO().getBlogUserWithNick(split[0]);

		if (entry == null) {
			Util.sendError(req, resp, "Invalid entry request.");
			return;
		}

		if (user == null) {
			Util.sendError(req, resp, "Invalid author request.");
			return;
		}

		if (!entry.getCreator().equals(user)) {
			Util.sendError(req, resp, "Entry does not match author.");
			return;
		}

		req.setAttribute("user", user);
		req.setAttribute("entry", entry);

		req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req, resp);
	}
}
