package hr.fer.zemris.java.blog.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Razred koji predstavlja jednog korisnika bloga.
 * Primarni kljuc je {@link #id}. 
 * Cuva se ime, prezime i nickname (unique), email, te
 * lista svih objava {@link BlogEntry}
 * @author Luka Dragutin
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogUser.getNick",query="select b from BlogUser as b where b.nick=:be"),
	@NamedQuery(name="BlogUser.getAll",query="select b from BlogUser as b")	
})
@Entity
@Table(name="blog_users")
@Cacheable(true)
public class BlogUser {

	/** Identifikator (primarni kljuc) */
	private long id;
	
	/**
	 * Korisnikovo ime
	 */
	private String firstName;
	
	/** Korisnikovo prezime */
	private String lastName;
	
	/** Korisnikov nadimak/korisnicko ime */
	private String nick;
	
	/** Korisnikov email */
	private String email;
	
	/** Korisnikova lozinka pohranjena u obliku hasha */
	private String passwordHash;
	
	/** Korisnikove objave na blogu */
	private List<BlogEntry> entries = new ArrayList<>();
	
	public BlogUser(long id, String firstName, String lastName, String nick, String email, String passwordHash) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nick = nick;
		this.email = email;
		this.passwordHash = passwordHash;
	}
	
	public BlogUser() {
	}

	/**
	 * @return the id
	 */
	@Id @GeneratedValue
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	@Column(length=20, nullable=false)
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
	@Column(length=30, nullable=false)
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
	@Column(nullable=false, unique=true)
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
	@Column(nullable=false)
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
	 * @return the passwordHash
	 */
	@Column(nullable=false)
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * @return the entries
	 */
	@OneToMany(mappedBy="creator",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("createdAt")
	public List<BlogEntry> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
	}
	
	/**
	 * Dodaje novu objavu na blogu
	 * @param entry Objava tipa {@link BlogEntry}
	 */
	public void addEntry(BlogEntry entry) {
		entries.add(entry);
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(nick);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BlogUser))
			return false;
		BlogUser other = (BlogUser) obj;
		return Objects.equals(nick, other.nick);
	}
}
