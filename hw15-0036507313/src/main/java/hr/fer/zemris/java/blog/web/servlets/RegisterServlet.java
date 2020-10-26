package hr.fer.zemris.java.blog.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogRegistry;
import hr.fer.zemris.java.blog.model.BlogUser;

@WebServlet(urlPatterns = {"/servleti/register"})
public class RegisterServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("form", new BlogRegistry());
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogRegistry bg = new BlogRegistry();
		bg.fillFromHttpRequest(req);
		if(bg.validate()) {
			if(DAOProvider.getDAO().getBlogUserWithNick(bg.getNick()) != null) {
				bg.addError("nick", "Nickname is already in use.");				
			}
			else {
				BlogUser user = new BlogUser();
				bg.fillUser(user);
				DAOProvider.getDAO().addBlogUser(user);
				resp.sendRedirect("main");
				return;
			}
		}
		req.setAttribute("form", bg);
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
}
