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

@WebServlet(urlPatterns = { "/servleti/login" })
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");
		req.setAttribute("login_nick_value", nick);

		if (nick == null || nick.isBlank()) {
			req.setAttribute("login_error", "Nickname is a required field.");
			req.getRequestDispatcher("main").forward(req, resp);
		}

		if (password == null || password.isBlank()) {
			req.setAttribute("login_error", "Password is a required field.");
			req.getRequestDispatcher("main").forward(req, resp);

		}

		String ep = Util.hexEncode(Util.calcHash(password));
		BlogUser blogUser = DAOProvider.getDAO().getBlogUserWithNick(nick);

		if (blogUser == null || !blogUser.getPasswordHash().equals(ep)) {
			req.setAttribute("login_error", "Invalid login credentials.");
			req.getRequestDispatcher("main").forward(req, resp);
		} else {
			var session = req.getSession();
			session.setAttribute("current_user_id", blogUser.getId());
			session.setAttribute("current_user_nick", blogUser.getNick());
			session.setAttribute("current_user_fn", blogUser.getFirstName());
			session.setAttribute("current_user_ln", blogUser.getLastName());
			resp.sendRedirect("main");
		}
	}
}
