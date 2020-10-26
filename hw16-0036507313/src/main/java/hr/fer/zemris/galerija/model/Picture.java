package hr.fer.zemris.galerija.model;

import java.util.Objects;


/**
 * Simple class that describes all the
 * attributes of an image from the image 
 * gallery. <p> Its fields are : <br>
 * 
 * name : The name of the picture,<br>
 * tag  : All the tags used to describe the picture, <br>
 * decs : A short description of the image.
 *  
 * @author LukaD
 *
 */
public class Picture {

	/**
	 * NAme of the picture
	 */
	private String name;
	
	/**
	 * Tags of the picture, used to
	 * find the picture more easily
	 */
	private String tag;
	
	/**
	 * Short description of the image
	 */
	private String desc;
	
	
	public Picture() {
	}

	/**
	 * Getter for {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for {@link #name}
	 */
	public void setName(String name) {
		this.name= name;
	}

	/**
	 * Getter for {@link #tag}
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Setter for {@link #tag}
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Getter for {@link #desc}
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Setter for {@link #desc}
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Picture))
			return false;
		Picture other = (Picture) obj;
		return Objects.equals(name, other.name);
	}
	
	
}
