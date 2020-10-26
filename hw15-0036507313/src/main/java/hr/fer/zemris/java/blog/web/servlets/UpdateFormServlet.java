package hr.fer.zemris.java.blog.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.blog.model.util.Util;

/**
 * Servlet koji od popunjenog formular za novu blog objavu
 * stvara novi objekt tipa {@link BlogEntry} ili azurira stari te
 * to prosljeduje u sloj za perzistenciju podataka
 * @author LukaD
 *
 */
@WebServlet(urlPatterns = { "/servleti/author/update" })
public class UpdateFormServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		String nick = req.getParameter("nick");
		String nickUser = (String) req.getSession().getAttribute("current_user_nick");

		if (!nick.equals(nickUser)) {
			Util.sendError(req, resp, "Access denied. Credentials not valid.");
			return;
		}

		BlogUser user = DAOProvider.getDAO().getBlogUser((Long) req.getSession().getAttribute("current_user_id"));

		BlogEntry entry = null;
		Date date = new Date();

		if (id != null) {
			try {
				entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(id));
			} catch (NumberFormatException e) {
				Util.sendError(req, resp, "Invalid request.");
			}
		} else {
			entry = new BlogEntry();
			entry.setCreator(user);
			entry.setCreatedAt(date);
		}

		entry.setLastModifiedAt(date);
		entry.setText(req.getParameter("text"));
		entry.setTitle(req.getParameter("title"));
		
		DAOProvider.getDAO().addBlogEntry(entry);
		DAOProvider.getDAO().refreshUser(user);
		
		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick());
	}
}
