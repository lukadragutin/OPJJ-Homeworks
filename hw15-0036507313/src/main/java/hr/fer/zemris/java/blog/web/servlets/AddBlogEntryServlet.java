package hr.fer.zemris.java.blog.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.blog.model.util.Util;

@WebServlet(urlPatterns = { "/alwaysNew" })
public class AddBlogEntryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String info = req.getServletPath().substring(1);
		String[] paths = info.split("/");
		String nick;
		
		if(paths.length != 4) {
			Util.sendError(req, resp, "Invalid request");
			return;
		}
		else {
			nick = paths[3];
		}

		String userNick = (String) req.getSession().getAttribute("current_user_nick");
		if (userNick == null || !nick.equals(userNick)) {
			Util.sendError(req, resp, "You do not have the needed credentials to access this site.");
			return;
		}

		BlogUser user = DAOProvider.getDAO().getBlogUser((Long) req.getSession().getAttribute("current_user_id"));
		req.setAttribute("user", user);
		req.setAttribute("entry", null);

		req.getRequestDispatcher("/WEB-INF/pages/blogEntryForm.jsp").forward(req, resp);
	}
}
