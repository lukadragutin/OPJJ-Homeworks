package hr.fer.zemris.java.blog.dao;

import hr.fer.zemris.java.blog.jpa.JPADAOImpl;

public class DAOProvider {

	private static DAO dao = new JPADAOImpl();
	
	public static DAO getDAO() {
		return dao;
	}
	
}