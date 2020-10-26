package hr.fer.zemris.java.blog.dao;

import java.util.List;

import hr.fer.zemris.java.blog.model.BlogComment;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;

public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
	/**
	 * Dohvaca korisnika sa zadanima id-em. Ako takav korisnik ne postoji vraca <code>null</code>
	 * @param id kljuc zapisa
	 * @return Korisnika ili <code>null</code> ako korisnik ne postoji
	 * @throws DAOException Ako dode do pogreski pri dohvatu podataka
	 */
	public BlogUser getBlogUser(Long id) throws DAOException;

	/**
	 * Dodaje korisnika u podatke
	 * @param blogUser Korisnik koji se dodaje
	 * @throws DAOException Ako dode do pogreske pri dodavanju
	 */
	public void addBlogUser(BlogUser blogUser) throws DAOException;

	/**
	 * Dohvaca korisnika sa zadanim nadimkom ili null ako nema takvog korisnika
	 * @param nick Trazeni nadimak
	 * @return Korisnika ili <code>null</code> ako nema korisnika
	 * @throws DAOException Ako dode do greske pri dohvatu
	 */
	public BlogUser getBlogUserWithNick(String nick) throws DAOException;
	
	public List<BlogUser> getBlogUsers() throws DAOException;

	public Long addBlogEntry(BlogEntry entry) throws DAOException;
	
	public void addBlogComment(BlogComment com) throws DAOException;

	public void refreshUser(BlogUser user) throws DAOException;
	
}