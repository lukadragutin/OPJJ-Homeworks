package hr.fer.zemris.java.blog.jpa;

import java.util.List;

import javax.persistence.Query;

import hr.fer.zemris.java.blog.dao.DAO;
import hr.fer.zemris.java.blog.dao.DAOException;
import hr.fer.zemris.java.blog.model.BlogComment;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getBlogUser(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
	}

	@Override
	public BlogUser getBlogUserWithNick(String nick) throws DAOException {
		try {
			return (BlogUser) JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getNick")
					.setParameter("be", nick).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void addBlogUser(BlogUser blogUser) throws DAOException {
		JPAEMProvider.getEntityManager().persist(blogUser);
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public List<BlogUser> getBlogUsers() throws DAOException {
		Query query = JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getAll", BlogUser.class);
		return query.getResultList();
	}

	@Override
	public Long addBlogEntry(BlogEntry entry) throws DAOException {
		Long id = entry.getId();
		var em = JPAEMProvider.getEntityManager();
		if (id == null) {
			em.persist(entry);
			id = entry.getId();
		} else {
			em.refresh(entry);
		}
		return id;
	}
	
	@Override
	public void addBlogComment(BlogComment com) throws DAOException {
		JPAEMProvider.getEntityManager().persist(com);
	}
	
	@Override
	public void refreshUser(BlogUser user) throws DAOException {
		JPAEMProvider.getEntityManager().refresh(user);
	}

}