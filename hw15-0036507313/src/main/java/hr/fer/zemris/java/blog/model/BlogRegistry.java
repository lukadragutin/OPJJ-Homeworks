package hr.fer.zemris.java.blog.model;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.blog.model.util.Util;

public class BlogRegistry {

	private String id;
	private String firstName;
	private String lastName;
	private String nick;
	private String email;
	private String password;
	private Map<String, String> errors = new HashMap<>();

	public BlogRegistry() {
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public void addError(String key, String error) {
		errors.put(key, error);
	}

	public String getError(String key) {
		return errors.get(key);
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public boolean hasError(String key) {
		return errors.get(key) != null;
	}

	public void fillUser(BlogUser user) {
		try {
			if (id != null) {
				user.setId(Long.parseLong(id));
			}
		} catch (NumberFormatException ignorable) {
		}
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPasswordHash(Util.hexEncode(Util.calcHash(password)));
		user.setNick(nick);
	}
	
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.id = prepare(req.getParameter("id"));
		this.firstName = prepare(req.getParameter("firstName"));
		this.lastName = prepare(req.getParameter("lastName"));
		this.email= prepare(req.getParameter("email"));
		this.nick = prepare(req.getParameter("nick"));
		this.password = req.getParameter("password");
	}

	public boolean validate() {
		boolean pass = true;
		if (firstName == null || firstName.isBlank()) {
			errors.put("firstName", "First name is a mandatory field");
			pass = false;
		}
		if (lastName == null || lastName.isBlank()) {
			errors.put("lastName", "Last name is a mandatory field");
			pass = false;
		}
		if (nick == null || nick.isBlank()) {
			errors.put("nick", "Nickname is a mandatory field");
			pass = false;
		}
		if(email == null || email.isBlank()) {
			errors.put("email", "Email is a mandatory field");
			pass = false;
		}
		if(password == null || password.isEmpty()) {
			errors.put("password", "Password is a mandatory field");
			pass = false;
		}
		return pass;
	}
	
	private String prepare(String s) {
		if(s==null) return "";
		return s.trim();
	}
}
