package hr.fer.zemris.java.blog.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogEntry;

@WebServlet("/servleti/prikazi")
public class PrikazBloga extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sID = req.getParameter("id");
		Long id = null;
		try {
			id = Long.valueOf(sID);
		} catch(Exception ignorable) {
		}
		if(id!=null) {
			BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
			if(blogEntry!=null) {
				req.setAttribute("blogEntry", blogEntry);
			}
		}
		req.getRequestDispatcher("/WEB-INF/pages/Prikaz.jsp").forward(req, resp);
	}
	
}