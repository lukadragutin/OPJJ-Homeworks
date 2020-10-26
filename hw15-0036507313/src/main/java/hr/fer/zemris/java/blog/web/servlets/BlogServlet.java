package hr.fer.zemris.java.blog.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogComment;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.blog.model.util.Util;


/**
 * Servlet koji se bavi svim zahtjevima koji se odnose na blogove i njiove objave/komentare
 * Prvo parsira o kojem je zahtjevu rijec pa poziva odredenu metodu
 * @author LukaD
 *
 */
@WebServlet(urlPatterns = { "/servleti/author/*" })
public class BlogServlet extends HttpServlet {

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

	/** Obrada zahtjeva
	 */
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] paths = req.getPathInfo().substring(1).split("/");
		String nick = paths[0];

		BlogUser user = DAOProvider.getDAO().getBlogUserWithNick(nick);

		if (user == null) {
			Util.sendError(req, resp, "Access denied.");
			return;
		}

		if (paths.length == 2) {
			if (paths[1].equals("new")) {
				newEntryPage(req, resp, user);
				return;
			} else if (paths[1].equals("edit")) {
				editEntryPage(req, resp, user);
				return;
			} else {
				showEntryPage(req, resp, paths[1], user);
				return;
			}
		} else if(paths.length == 3) {
			newComment(req, resp, paths[1], user);
			return;
		} 
		else {
			req.setAttribute("user", user);
			req.setAttribute("entries", user.getEntries());
			req.getRequestDispatcher("/WEB-INF/pages/blog.jsp").forward(req, resp);
		}
	}

	/**
	 * Dodaje novi komentar u sloj za perzistenciju podataka. 
	 * Ako korisnik nije logiran moze i dalje komentirati ako unese
	 * svoju email adresu.
	 * @param req http zahtjev
	 * @param resp http odgovor
	 * @param sID ID blog entry-a gdje se komentar objavljuje
	 * @param user Korisnik cija je objava, autor {@link BlogEntry}
	 * @throws IOException Ako dode do greske prilikom povezivanja
	 * @throws ServletException 
	 */
	private void newComment(HttpServletRequest req, HttpServletResponse resp, String sID, BlogUser user) throws IOException, ServletException {
		String comment = req.getParameter("comment");
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(sID));
		
		req.setAttribute("user", user);
		req.setAttribute("blogEntry", entry);
		
		if(comment == null || comment.isBlank()) {
			req.setAttribute("comment_error", "Cannot submit empty comment.");
			req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req, resp);
			return;
		}
		Long id = (Long) req.getSession().getAttribute("current_user_id");
		String email;
		if(id == null) {
			email = req.getParameter("email");
			if(email == null) {
				req.setAttribute("comment_error", "Email must be entered.");
				req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req, resp);
				return;
			}
		}
		else {
			email = DAOProvider.getDAO().getBlogUser(id).getEmail();
		}
		
		BlogComment bc = new BlogComment();
		bc.setBlogEntry(entry);
		bc.setMessage(comment);
		bc.setUsersEMail(email);
		bc.setPostedOn(new Date());

		
		DAOProvider.getDAO().addBlogComment(bc);
		
		String path = req.getContextPath() + "/servleti/author/" + user.getNick() + "/" + entry.getId();
		resp.sendRedirect(path);
	}

	
	/**
	 * Poziva stranicu za ispunu nove blog objave
	 * @param req Http zahtjev
	 * @param resp http odgovor
	 * @param user Korisnik koji pise novu objavu
	 * @throws ServletException 
	 * @throws IOException
	 */
	private void newEntryPage(HttpServletRequest req, HttpServletResponse resp, BlogUser user)
			throws ServletException, IOException {
		String userNick = (String) req.getSession().getAttribute("current_user_nick");

		if (userNick == null || !user.getNick().equals(userNick)) {
			Util.sendError(req, resp, "You do not have the needed credentials to access this site.");
			return;
		}

		req.setAttribute("user", user);
		req.setAttribute("entry", null);

		req.getRequestDispatcher("/WEB-INF/pages/blogEntryForm.jsp").forward(req, resp);
	}

	/**
	 * Poziva stranicu za azuriranje postojece objave
	 * @param req http zahtjev
	 * @param resp http odgovor
	 * @param user KOrisnik koji radi izmjene
	 * @throws ServletException
	 * @throws IOException
	 */
	private void editEntryPage(HttpServletRequest req, HttpServletResponse resp, BlogUser user)
			throws ServletException, IOException {
		String id = req.getParameter("id");
		Long eID;
		try {
			eID = Long.parseLong(id);
		} catch (NumberFormatException e) {
			Util.sendError(req, resp, "Invalid entry request.");
			return;
		}

		String userNick = (String) req.getSession().getAttribute("current_user_nick");

		if (userNick == null || !user.getNick().equals(userNick)) {
			Util.sendError(req, resp, "You do not have the needed credentials to access this site.");
			return;
		}

		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(eID);

		if (entry == null) {
			Util.sendError(req, resp, "Invalid entry request.");
			return;
		}

		if (!entry.getCreator().equals(user)) {
			Util.sendError(req, resp, "Entry does not match user.");
			return;
		}

		req.setAttribute("user", user);
		req.setAttribute("entry", entry);

		req.getRequestDispatcher("/WEB-INF/pages/blogEntryForm.jsp").forward(req, resp);
	}

	/**
	 * Poziva stranicu za prikaz odredene objave i komentara
	 * na tu objavu
	 * @param req http zahtjev
	 * @param resp http odgovor
	 * @param id Identifikator Objave
	 * @param user Korisnik autor objave
	 * @throws ServletException 
	 * @throws IOException
	 */
	private void showEntryPage(HttpServletRequest req, HttpServletResponse resp, String id, BlogUser user)
			throws ServletException, IOException {
		Long eID;
		try {
			eID = Long.parseLong(id);
		} catch (NumberFormatException e) {
			Util.sendError(req, resp, "Invalid entry request.");
			return;
		}

		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(eID);

		if (entry == null) {
			Util.sendError(req, resp, "Invalid entry request.");
			return;
		}

		if (!entry.getCreator().equals(user)) {
			Util.sendError(req, resp, "Entry does not match author.");
			return;
		}

		req.setAttribute("user", user);
		req.setAttribute("blogEntry", entry);

		req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req, resp);

	}
}
